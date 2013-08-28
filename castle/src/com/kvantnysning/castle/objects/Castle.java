package com.kvantnysning.castle.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Castle implements Graphics {
	
	Bitmap bitmap;
	
	protected double health = 100;
	
	public Castle(Bitmap bitmap) {
		
		this.bitmap = bitmap;
		
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	
	public void dealDamage(double dmg){
		
		health -= dmg;

	}
	
	public boolean isDead(){
		return health<0;
	}

}
