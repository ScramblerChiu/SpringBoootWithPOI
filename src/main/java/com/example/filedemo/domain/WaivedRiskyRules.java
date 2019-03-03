package com.example.filedemo.domain;

public class WaivedRiskyRules {

	private String firewallID;
	private String ruleID;
	private String comment;
	
	public String getFirewallID() {
		return firewallID;
	}
	public void setFirewallID(String firewallID) {
		this.firewallID = firewallID;
	}
	public String getRuleID() {
		return ruleID;
	}
	public void setRuleID(String ruleID) {
		this.ruleID = ruleID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "WaivedRiskyRules [firewallID=" + firewallID + ", ruleID=" + ruleID + ", comment=" + comment + "]";
	}

	
	
	
}
