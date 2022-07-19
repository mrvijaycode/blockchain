package main

import (
	"encoding/json"
	"fmt"
	"log"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"

	//"math/rand"
	"strconv"
	"time"
)

func main() {
	err := shim.Start(new(Chaincode))
	if err != nil {
		fmt.Printf("Error on start: %s", err)
	}
}

// Chaincode is the definition of the chaincode structure.
type Chaincode struct {
}

//Header for each record
type Header struct {
	EpochID       int64  `json:"epochid"`
	ChannelLedger string `json:"channelledger"`
	ID            string `json:"id"` //if no external id then a random id is assigned
	Type          string `json:"rectype"`
}

func (cc *Chaincode) Init(stub shim.ChaincodeStubInterface) sc.Response {
	fcn, params := stub.GetFunctionAndParameters()
	fmt.Println("Init()", fcn, params)
	return shim.Success([]byte("INSTANTIATION SUCCESS"))
}

func (cc *Chaincode) Invoke(stub shim.ChaincodeStubInterface) sc.Response {
	fcn, params := stub.GetFunctionAndParameters()
	fmt.Println("Invoke(): Function-" + fcn)
	if fcn == "initRequirmentRecord" {
		//return cc.initRequirmentRecord(stub, params)
	} else if fcn == "getRequirmentRecordByuserId" { //query requirment record by userId
		return cc.getRequirmentRecordByuserId(stub, params)
	} else if fcn == "createRequirmentRecord" {
		return cc.createRequirmentRecord(stub, params)
	} else if fcn == "deleteRequirmentRecordByuserId" {
		return cc.deleteRequirmentRecordByuserId(stub, params)
	} else if fcn == "getRequirmentRecordByFilter" {
		return cc.getRequirmentRecordByFilter(stub, params)
	}
	fmt.Println("invoke did not find func: " + fcn) //error
	return shim.Error("Received unknown function invocation")
}s

// Get requirmentdetails by userId
func (cc *Chaincode) getRequirmentRecordByuserId(stub shim.ChaincodeStubInterface, args []string) sc.Response {
	var logger = shim.NewLogger("getRequirmentRecordByuserId")
	logger.Info("Inside getRequirmentRecordByuserId")
	var keyval, jsonResp string
	var err error
	if len(args) != 1 {
		return shim.Error("Insufficient arguments. Please pass userId")
	}
	fmt.Println("getRequirmentRecordByuserId called")
	keyval = args[0]
	valAsbytes, err := stub.GetState(keyval)
	if err != nil {
		jsonResp = "{\"Error\":\"Failed to get state for " + keyval + "\"}"
		return shim.Error(jsonResp)
	} else if valAsbytes == nil {
		jsonResp = "{\"Error\":\" does not exist: " + keyval + "\"}"
		return shim.Error(jsonResp)
	}
	return shim.Success(valAsbytes)
}

// Get requirmentdetails by Filter
func (cc *Chaincode) getRequirmentRecordByFilter(stub shim.ChaincodeStubInterface, args []string) sc.Response {
	var logger = shim.NewLogger("getRequirmentRecordByFilter")
	fmt.Println("Inside getRequirmentRecordByFilter")
	var jsonResp string
	var err error
	if len(args) != 1 {
		return shim.Error("Insufficient arguments. Please pass userId")
	}
	fmt.Println("getRequirmentRecordByFilter called")
	keyval := args[0]
	resultIterator, err := stub.GetQueryResult(keyval)
	if err != nil {
		jsonResp = "{\"Error\":\"Failed to get state for " + keyval + "\"}"
		return shim.Error(jsonResp)
	} else if resultIterator == nil {
		jsonResp = "{\"Error\":\" does not exist: " + keyval + "\"}"
		return shim.Error(jsonResp)
	}

	logger.Info("result Iterator", resultIterator)
	fmt.Printf("Result Iterator: %+v\n", resultIterator)
	var recObj interface{}
	for resultIterator.HasNext() {
		resObj, err2 := resultIterator.Next()
		if err2 != nil {
			//return nil, err
			fmt.Println(err2)
		}
		fmt.Printf("%+v\n", resObj)
		fmt.Println("resObj:==>  ", resObj)
		strJson := resObj.Value
		errRead := json.Unmarshal([]byte(strJson), &recObj)
		if errRead != nil {
			log.Println("Error in read")
			//return shim.Error("Error in Putstate")
		}
		fmt.Println("recObj:==>  ", recObj)
	}
	//fmt.Println("jsonResp  ", jsonResp)
	ingestableBytes, err := json.Marshal(recObj)

	if err != nil {
		fmt.Println("Error in marshalling")
		log.Println("Error in marshalling")
		return shim.Error("Error in marshalling")
	}
	fmt.Println("ingestableBytes  ", string(ingestableBytes))
	return shim.Success(ingestableBytes)
}

// =============================
// Init - Create Requirment Record
// =============================
func (cc *Chaincode) createRequirmentRecord(stub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 1 {
		return shim.Error("ARGUMENTS NOT MATCHING")
	}

	fmt.Println("Inside createIngestable, args received", args)

	// Get Epoch Time
	now := time.Now()
	secs := now.Unix()

	strJson := args[0]

	fmt.Println("strJson", strJson)

	var recObj interface{}
	errRead := json.Unmarshal([]byte(strJson), &recObj)

	if errRead != nil {
		fmt.Println("Error in read")
	}

	putObj := recObj.(map[string]interface{})

	secs := now.Unix()

	epid := strconv.FormatInt(secs, 10)
	saveid := "DLT" + epid
	header := Header{
		EpochID:       secs,
		ChannelLedger: "commonchannel",
		Type:          "Requirement",
		ID:            saveid,
	}

	putObj["Header"] = header

	//fmt.Println("\nITEMID: ", itemid)
	//fmt.Printf("\n%+v\n ", recObj)

	recordationeBytes, err2 := json.Marshal(putObj)

	if err2 != nil {
		fmt.Println("error in compliance creation", err2)
	} else {
		fmt.Println(string(recordationeBytes))
	}

	//epid := strconv.FormatInt(secs, 10)

	strid := putObj["RequirementRecID"].(string)

	fmt.Println("strid == >", strid)

	//err1 := stub.PutState("DLT"+epid, recordationeBytes)
	err1 := stub.PutState(strid, recordationeBytes)
	if err1 != nil {
		log.Println("Error in putstate")
		return shim.Error("Error in Putstate")
	}

	fmt.Println("Putstate completed!")
	printmsg := strid + " item created!"
	return shim.Success([]byte(printmsg))
}

func (cc *Chaincode) deleteRequirmentRecordByuserId(stub shim.ChaincodeStubInterface, args []string) sc.Response {
	var logger = shim.NewLogger("deleteRequirmentRecordByuserId")
	logger.Info("Inside deleteRequirmentRecordByuserId")
	var keyval string
	// var err error
	if len(args) != 1 {
		return shim.Error("Insufficient arguments. Please pass userId")
	}
	fmt.Println("deleteRequirmentRecordByuserId called")
	keyval = args[0]
	err := stub.DelState(keyval)
	if err != nil {
		fmt.Println("Error in delstate")
		log.Println("Error in delstate")
		return shim.Error(err.Error())
	}

	if err != nil {
		return shim.Error(fmt.Sprintf("Faild to emit deleteRequirment!"))
	}

	fmt.Println("Delstate completed!")
	return shim.Success(nil)
}
