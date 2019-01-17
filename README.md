# MAutomaton

This is an attempt to tackle the daily tasks in NGS data analysis. This tool is written in Java Swing and has below features.

1. VCF comparison utility: To compare SNP, Indels, CNVs in VCF_4.2 format.
2. Duplicate metrics utility: To calculate %duplicates from a BAM file.
3. VCF Ts/Tv util: To calculate transition and transversion ratio from given VCF file
4. Comparison of QC metrics in the form of .properties file.


## How to use

### 1. VCF comparator

Prerequisite: **Java 1.8** or **_higher_**

#### Steps
1. Right click on the jar file and open with java
2. In **"VCF comparator"** tab select the VCF 1, VCF 2 files to compare.
3. Click **"Compare"**
4. Based on size of the VCF files it will take time to compare the VCF. in the mean time the UI will _freeze_ as its a single threaded application.
5. After comparison is done the UI will update with the comparison results as displayed in below screenshot
6. If you are interested to find out the actual mismatching and common variants in VCF format visit "C:\gatKAutomator\comparisonResults" directory of your computer, not to mentione you need to have read/write access to this location.The result VCF are named in following way,

..* Common variants in both vcf: <VCF 1>_common.vcf
..* Insertion variants only in VCF 1: <VCF 1>_insOld.vcf
* Insertion variants only in VCF 2: <VCF 2>_insNew.vcf
* Deletion variants only in VCF 1: <VCF 1>_delOld.vcf
* Deletion variants only in VCF 2: <VCF 2>_delNew.vcf
* SNP variants only in VCF 1: <VCF 1>_snpOld.vcf
* SNP variants only in VCF 2: <VCF 2>_snpNew.vcf
