package roundoid.classes.game;

class Speed {
	
	public static final int DIRECTION_RIGHT	= 1;
	public static final int DIRECTION_LEFT	= -1;
	public static final int DIRECTION_UP	= -1;
	public static final int DIRECTION_DOWN	= 1;
	
	
	public static float fXCoordinate3 = -0.85f;
	public static float fYCoordinate3 = 0.1f;
	
	public static float fXCoordinateTranslated;
	public static float fYCoordinateTranslated;
	
	public static float xv = 0.2f;	// velocity value on the X axis
	public static float yv = 0.2f;	// velocity value on the Y axis
	
	public static float xDirection = -0.11f;
	public static float yDirection = 0.1f;
	
	//asdf sdfgsdfg hjhjk ghj sdfg sdfgh shkkl
	
	
	public Speed() {
		//this.xv = 1;
		//this.yv = 1;		
	}

	public Speed(float xv, float yv) {
	    setXv(xv);
		setYv(yv) ;
	}

	public static float getXv() {
		return xv;
	}

	public static void setXv(float xv) {
		Speed.xv = xv;
	}
	
	public static float getYv() {
		return yv;
	}

	public static void setYv(float yv) {
		Speed.yv = yv;
	}

	public static float getxDirection() {
		return xDirection;
	}
	public static void setxDirection(int _xDirection) {
		xDirection = _xDirection;
	}
	public static float getyDirection() {
		return yDirection;
	}
	public static void setyDirection(int _yDirection) {
		yDirection = _yDirection;
	}

	// changes the direction on the X axis
	public static void toggleXDirection() {
		xDirection = xDirection * -1;	
	}	
	
	// changes the direction on the Y axis
	public static void toggleYDirection() {
		yDirection = yDirection * -1;	
	}
	
	public static void Update()
	{
		fXCoordinate3 += (getXv() * getxDirection()); 
		fYCoordinate3 += (getYv() * getyDirection());
		
		if(fXCoordinate3 <= -1.0f || fXCoordinate3 > 1.0)
			toggleXDirection();
		if(fYCoordinate3 > 1.0f || fYCoordinate3 < -1.0f)
			toggleYDirection();		
	}
}
