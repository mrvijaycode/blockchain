'use strict';

const { Contract } = require('fabric-contract-api');

class testContract extends Contract {

    async queryMarks(ctx, studentId) {
        let marksAsBytes = await ctx.stub.getState(studentId);
        if (!marksAsBytes || marksAsBytes.toString().length <= 0) {
            throw new Error('Student this Id does not exist:');
        }
        let marks = JSON.parse(marksAsBytes.toString());
        return JSON.stringify(marks);
    }

    async addMarks(ctx, studentId, sub1, sub2, sub3) {
        let marks = {
            subj1: sub1,
            subj2: sub2,
            subj3: sub3
        };
        await ctx.stub.putState(studentId, Buffer.from(JSON.stringify(marks)));
        console.log('Student marks added to the ledger..');
    }

    async deleteMarks(ctx, studentId) {
        await ctx.stub.deleteState(studentId);
        console.log('Student marks deleted from the ledger..')
    }

}

module.exports = testContract;
