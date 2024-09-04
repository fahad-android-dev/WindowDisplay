package com.orbits.queuingsystem;

import java.io.File;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CompanyLogo extends Fragment {
	View companyLogo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		companyLogo = inflater.inflate(R.layout.activity_company_logo, container, true);
		return companyLogo;
	}

	@Override
	public void onStart() {
		super.onStart();
		setCompanyLogo();
	}

	// load company logo from sdcard.
	public void setCompanyLogo() {
		String imageInSD = MainActivity.sdcardPath + "/Queue_Config Files/logo.png";
		File imgPath = new File(imageInSD);
		if(imgPath.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			ImageView myImageView = (ImageView) companyLogo.findViewById(R.id.imgCompanyLogo);
			myImageView.setImageBitmap(bitmap);
		}
	}
}
