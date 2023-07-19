/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author sajid.ali
 */
public class ChangeStubs {
    private int stubId;

    public int getStubId() {
        return stubId;
    }

    public void setStubId(int stubId) {
        this.stubId = stubId;
    }

    public int getChangeId() {
        return changeId;
    }

    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }
    private int changeId;
    private int issueId;
}
