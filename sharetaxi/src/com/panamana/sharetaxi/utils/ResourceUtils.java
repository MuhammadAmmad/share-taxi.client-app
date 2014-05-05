package com.panamana.sharetaxi.utils;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * resource utilities class
 * @author naama
 */
public class ResourceUtils {

	/**
	 * get BitmapDescriptor from image resource.
	 * @param imageResource
	 * @return BitmapDescriptor
	 */
	public static BitmapDescriptor getImage(int imageResource) {
		 return BitmapDescriptorFactory.fromResource(imageResource);
	}
	
}
