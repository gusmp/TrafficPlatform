package org.trafficplatform.anprserver.service.plataInfoProviders;

public enum PlateInfoProvider {
	
	DUMMY(AnprProviderNames.DUMMY),
	ANPR_CLOUD_ANPR(AnprProviderNames.ANPR_CLOUD_ANPR),
	ANPR_CLOUD_MMR(AnprProviderNames.ANPR_CLOUD_MMR),
	VIALSECUREWS(AnprProviderNames.VIALSECUREWS);
	
	public class AnprProviderNames{
        public static final String DUMMY = "DUMMY";
        public static final String ANPR_CLOUD_ANPR = "ANPR_CLOUD_ANPR";
        public static final String ANPR_CLOUD_MMR = "ANPR_CLOUD_MMR";
        public static final String VIALSECUREWS = "VIALSECUREWS";
    }
	 
	private final String anprProviderName;
	
	
	private PlateInfoProvider(String anprProviderName) {
		this.anprProviderName = anprProviderName;
	}
	
	public String toString() {
		return this.anprProviderName;
	}
}
