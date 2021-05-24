package org.trafficplatform.anprserver.service.plataInfoProviders.dummy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.trafficplatform.anprserver.bean.api.response.PlateInfo;
import org.trafficplatform.anprserver.service.plataInfoProviders.IPlateInfoProvider;
import org.trafficplatform.anprserver.service.plataInfoProviders.PlateInfoProvider;

@Service(value = PlateInfoProvider.AnprProviderNames.DUMMY)
public class DummyPlateInfoProvider implements IPlateInfoProvider {

	@Override
	public List<PlateInfo> getPlateInfo(Resource imageResource) {
		
		List<PlateInfo> plateInfoList = new ArrayList<PlateInfo>();
		
		PlateInfo testPlate = new PlateInfo("1111AAA", 0.0, 10, 10, 20, 20, 30, 30, 40, 40, "Car maker", "Model", "Category", 0.0);
		plateInfoList.add(testPlate);
		
		return plateInfoList;
	}
}
