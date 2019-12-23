package com.radius.sodalityUser.model;

import java.util.Date;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Unit")

public class Unit {

	@Id()
	@GeneratedValue()
	private Long id;

	private String unit_no;
	private String Description;
	private String unit_type;
	private Date createDate;
	private Date updateDate;
	private String uuid;
	@Relationship(type = "UNDER", direction = Relationship.UNDIRECTED)
	public Tower parentAccount;
	private int sanctionedLoadGrid;
	private int sanctionedLoadBackUp;
	private String soldStatus;
	private String UOM;
	private Boolean pipeGas;
	
	public Boolean getPipeGas() {
		return pipeGas;
	}

	public void setPipeGas(Boolean pipeGas) {
		this.pipeGas = pipeGas;
	}

	@Relationship(type = "UNIT_TYPE", direction = Relationship.UNDIRECTED)
	public UnitType unit_remark;

	public UnitType getUnit_remark() {
		return unit_remark;
	}

	public void setUnit_remark(UnitType unit_remark) {
		this.unit_remark = unit_remark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUnit_no() {
		return unit_no;
	}

	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUnit_type() {
		return unit_type;
	}

	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;
	}

	public Tower getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(Tower parentAccount) {
		this.parentAccount = parentAccount;
	}

 
	public int getSanctionedLoadGrid() {
		return sanctionedLoadGrid;
	}

	public void setSanctionedLoadGrid(int sanctionedLoadGrid) {
		this.sanctionedLoadGrid = sanctionedLoadGrid;
	}

	public int getSanctionedLoadBackUp() {
		return sanctionedLoadBackUp;
	}

	public void setSanctionedLoadBackUp(int sanctionedLoadBackUp) {
		this.sanctionedLoadBackUp = sanctionedLoadBackUp;
	}

	public String getSoldStatus() {
		return soldStatus;
	}

	public void setSoldStatus(String soldStatus) {
		this.soldStatus = soldStatus;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
	}

}
