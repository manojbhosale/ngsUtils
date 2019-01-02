package com.psl.automation.fileCompare;

import htsjdk.variant.variantcontext.VariantContext;

public class VCFrecord {


	private String chromosome;
	private String ref;
	private String alt;
	private Long position;
	private String type ;
	private VariantContext context;
	private String validationType;

	public VCFrecord(String chromosome, String ref, String alt, Long position, String validationType) {
		super();
		this.chromosome = chromosome;
		this.ref = ref;
		this.alt = alt;
		this.position = position;
		this.validationType = validationType;
		if(ref.length() == alt.length()){

			this.type = "SNP";

		}else if(ref.length() > alt.length()){

			this.type = "Deletion";

		}else if(ref.length() < alt.length()){

			this.type = "Insertion";

		}
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	public VariantContext getContext() {
		return context;
	}

	public void setContext(VariantContext context) {
		this.context = context;
	}

	public VCFrecord(String chromosome, String ref, String alt, Long position,
			VariantContext context,String validationType) {
		super();
		this.chromosome = chromosome;
		this.ref = ref;
		this.alt = alt;
		this.position = position;
		this.context = context;
		this.validationType = validationType;

		if(ref.length() == alt.length()){

			this.type = "SNP";

		}else if(ref.length() > alt.length()){

			this.type = "Deletion";

		}else if(ref.length() < alt.length()){

			this.type = "Insertion";

		}
	}

	public String getValidationType() {
		return validationType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	@Override
	public boolean equals(Object obj){

		if(obj instanceof VCFrecord){
			VCFrecord vcr = (VCFrecord)obj;
			if(vcr.getChromosome().equals(this.chromosome) && vcr.getRef().equals(this.ref) && vcr.getAlt().equals(this.alt) && vcr.getPosition().equals(this.position))
				return true;
			return false;
		}else{
			System.out.println(1);
			return false;
		}


	}
	
	@Override
	public int hashCode() {
	
		int hash = 1;
		hash = hash * 17 + chromosome.hashCode();
		hash = hash * 31 + ref.hashCode();
		hash = hash * 37 + alt.hashCode();
		hash = hash * 43 + position.hashCode();
				
		return hash;
		
	}
	

	@Override
	public String toString() {
		return "VCFrecord [chromosome=" + chromosome + ", ref=" + ref
				+ ", alt=" + alt + ", position=" + position + ", type=" + type
				+ ", context=" + context + ", validationType=" + validationType
				+ "]";
	}

}
