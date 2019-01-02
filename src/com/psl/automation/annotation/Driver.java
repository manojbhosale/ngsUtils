package com.psl.automation.annotation;

import java.io.File;

public class Driver {

	public static void main(String[] args) {
		VariationReporter vr = new VariationReporter();
		try {
			System.out.println(vr.analyze(9606, "GCF_000001405.25", "", new File("C:\\Users\\manojkumar_bhosale\\Desktop\\TestVariationreporter\\vcf.vcf")));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
