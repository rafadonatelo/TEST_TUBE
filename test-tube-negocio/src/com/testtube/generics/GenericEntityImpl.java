package com.testtube.generics;

import javax.persistence.Transient;

import com.testtube.entidade.IGenericEntity;

public abstract class GenericEntityImpl implements IGenericEntity, Cloneable {

	/**
	 * Gerado automaticamente
	 */
	private static final long serialVersionUID = 216443380150164191L;

	private static long MEMORY_ID_GENERATOR;

	@Transient
	private Long memoryId;

	{
		memoryId = MEMORY_ID_GENERATOR++;
	}

	@Override
	public int hashCode() {
		return (getId() == null) ? memoryId.intValue() : getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		GenericEntityImpl other = (GenericEntityImpl) obj;
		if (getId() != null && other.getId() != null) {
			return getId().equals(other.getId());
		}

		if (getId() == null && other.getId() == null) {
			return memoryId.equals(other.memoryId);
		}

		return false;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning not allowed.");
			return this;
		}
	}
}
