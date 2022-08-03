'use strict';

const { Contract } = require('fabric-contract-api');

class Chaincode extends Contract {

	async checkEntityType(ctx, id, docType) {
		try {
			let entityState = await ctx.stub.getState(id);
			if (!entityState || entityState.length === 0) {
				return false;
			} else {
				let entity = {};
				entity = JSON.parse(entityState.toString());
				if (entity["docType"] === docType) {
					return true;
				} else {
					return false;
				}
			}
		} catch (err) {
			return false;
		}
	}

	// fulfillment
	async RegisterFulfillment(ctx, fulfillmentDetails) {
		try {
			let fulfillment = JSON.parse(fulfillmentDetails);
			if (await this.checkEntityType(ctx, fulfillment["id"], "fulfillment")) {
				throw new Error(`Fulfillment exists`);
			}
			fulfillment["docType"] = "fulfillment";
			fulfillment["hash"] = ctx.stub.getTxID();
			await ctx.stub.putState(fulfillment["id"], Buffer.from(JSON.stringify(fulfillment)));
		} catch (error) {
			let jsonResp = {};
			jsonResp.error = 'Failed to decode JSON of: ' + fulfillment["id"];
			throw new Error(jsonResp);
		}
	}

	async GetFulfillment(ctx, id) {
		const fulfillmentState = await ctx.stub.getState(id);
		if (!await this.checkEntityType(ctx, id, "fulfillment")) {
			throw new Error(`Fulfillment does not exist`);
		}
		return fulfillmentState.toString();
	}

	async UpdateFulfillment(ctx, fulfillmentDetails) {
		try {
			let fulfillment = JSON.parse(fulfillmentDetails);
			if (!await this.checkEntityType(ctx, fulfillment["id"], "fulfillment")) {
				throw new Error(`Fulfillment does not exist`);
			}
			fulfillment["docType"] = "fulfillment";
			let fulfillmentState = Buffer.from(JSON.stringify(fulfillment));
			await ctx.stub.putState(fulfillment["id"], fulfillmentState);
		} catch (error) {
			throw new Error(error);
		}
	}

	async DeleteFulfillment(ctx, id) {
		if (!id) {
			throw new Error('Fulfillment id must not be empty');
		}

		let selector = {
			docType: "fulfillment",
			id: id
		}
		let fulfillment = await this.QueryResults(ctx, JSON.stringify({ selector: selector }));
		fulfillment = JSON.parse(fulfillment);
		if (fulfillment.length === 0) {
			throw new Error(`Fulfillment does not exist`);
		}
		fulfillment = fulfillment[0].Record;
		fulfillment["deleted"] = true;
		await ctx.stub.putState(id, Buffer.from(JSON.stringify(fulfillment)));
		return true;
	}

	// File Meta Data
	async SaveFileMetaData(ctx, fileMetaData) {
		try {
			let fileMetadata = JSON.parse(fileMetaData);
			if (!await this.checkEntityType(ctx, fileMetadata["id"], "fulfillment")) {
				throw new Error(`Fulfillment does not exist`);
			}
			fileMetadata["docType"] = "fileMetaData";
			fileMetadata["hash"] = ctx.stub.getTxID();
			await ctx.stub.putState(fileMetadata["hash"], Buffer.from(JSON.stringify(fileMetadata)));
			return fileMetadata["hash"];
		} catch (error) {
			throw new Error(error);
		}
	}

	async FileHashExists(ctx, hash) {
		try {
			let selector = {
				docType: "fileMetaData",
				hash: hash
			}
			let fileMetaData = await this.QueryResults(ctx, JSON.stringify({ selector: selector }));
			fileMetaData = JSON.parse(fileMetaData);
			if (fileMetaData.length === 0) {
				throw new Error(`File Meta Data does not exist`);
			}
			return true;
		} catch (error) {
			throw new Error(error);
		}
	}

	async DeleteFileMetaData(ctx, hash) {
		try {
			let selector = {
				docType: "fileMetaData",
				hash: hash
			}
			let fileMetaData = await this.QueryResults(ctx, JSON.stringify({ selector: selector }));
			fileMetaData = JSON.parse(fileMetaData);
			if (fileMetaData.length === 0) {
				throw new Error(`File Meta Data does not exist`);
			}
			fileMetaData = fileMetaData[0].Record;
			fileMetaData["deleted"] = true;
			await ctx.stub.putState(hash, Buffer.from(JSON.stringify(fileMetaData)));
			return true;
		} catch (error) {
			throw new Error(error);
		}
	}

	// Query
	async GetQueryResultForQueryString(ctx, queryString) {
		let resultsIterator = await ctx.stub.getQueryResult(queryString);
		let results = await this.GetAllResults(resultsIterator, true);
		return JSON.stringify(results);
	}

	async GetAllResults(iterator, isHistory) {
		let allResults = [];
		let res = await iterator.next();
		while (!res.done) {
			if (res.value && res.value.value.toString()) {
				let jsonRes = {};
				if (isHistory && isHistory === true) {
					jsonRes.TxId = res.value.tx_id;
					jsonRes.Timestamp = res.value.timestamp;
					jsonRes.response = res;
					try {
						jsonRes.Value = JSON.parse(res.value.value.toString('utf8'));
					} catch (err) {
						console.log(err);
						jsonRes.Value = res.value.value.toString('utf8');
					}
				} else {
					jsonRes.Key = res.value.key;
					try {
						jsonRes.Record = JSON.parse(res.value.value.toString('utf8'));
					} catch (err) {
						console.log(err);
						jsonRes.Record = res.value.value.toString('utf8');
					}
				}
				allResults.push(jsonRes);
			}
			res = await iterator.next();
		}
		iterator.close();
		return allResults;
	}

	async QueryResults(ctx, queryString) {
		let iterator = await ctx.stub.getQueryResult(queryString);
		let results = [];
		let res = await iterator.next();
		while (!res.done) {
			if (res.value && res.value.value.toString()) {
				let result = {};
				result.Key = res.value.key;
				try {
					result.Record = JSON.parse(res.value.value.toString('utf8'));
				} catch (err) {
					console.log(err);
					result.Record = res.value.value.toString('utf8');
				}
				results.push(result);
			}
			res = await iterator.next();
		}
		iterator.close();
		return JSON.stringify(results);
	}

}

module.exports = Chaincode;