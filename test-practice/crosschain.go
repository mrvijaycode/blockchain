import (
	"encoding/json"
	"fmt"
)

func (cc *Chaincode) GetReference(stub shim.ChaincodeStubInterface, refid string, funcName string, chainName string) interface{} {

	params := []string{funcName, refid}
	fmt.Println("\nCROSSCHAIN PARAMS: ==>", cc.marshalStr(params))
	queryArgs := make([][]byte, len(params))
	for i, arg := range params {
		queryArgs[i] = []byte(arg)
	}
	response := stub.InvokeChaincode(chainName, queryArgs, ChannelName)
	if response.Status != shim.OK {
		fmt.Errorf("Failed to query chaincode. Got error: %s", response.Payload)
	}
	var refObj interface{}
	errRead2 := json.Unmarshal([]byte(string(response.Payload)), &refObj)
	if errRead2 != nil {
		fmt.Println("Error in read Employee Unmarshal details")
	}
	return refObj
}


//ABOVE CODE WILL BE CALLED BY THIS BELOW CODE

if isRecordation {
	m["Header"] = header

	if userId != "" {
		empResponse := cc.GetReference(stub, userId, getEmployeeRecordByuserId, EmployeeChain)
		putObj["EmployeeDetails"] = empResponse

		if rectype == "compliance" && TrainingStatus == "Complete" {
			//filters := "{\"selector\":{\"RequirementuserId\":\"" + userId + "\"}}"
			filters := "{\"selector\":{\"RequirementuserId\":\"" + userId + "\",\"RequirementActivityCode\":\"" + TrainingActivityCode + "\",\"RequirementTrainingSourceCode\":\"" + TrainingActivitySourceCode + "\",\"RequiredTrainingOrganization\":\"" + TrainingOrganization + "\"}}"
			reqResponse := cc.GetReference(stub, filters, getRequirmentRecordByFilter, RequirementChain)
			fmt.Println("\nREQ Query RESULT==>", cc.marshalStr(reqResponse))
			putObj["RequirementsDetails"] = reqResponse
		}
		putObj[RECTYPE] = m
		retunObj := cc.saveToDLT(stub, saveid, putObj, "RECORDATION")

		if retunObj != nil {
			err4 := stub.SetEvent("recordation", retunObj)
			if err4 != nil {
				return shim.Error(fmt.Sprintf("Faild to emit recordation!"))
			} else {
				fmt.Println("\n Recordation event fired.. !")
			}
		}

	} else {
		fmt.Println("NO userId")
	}

}
