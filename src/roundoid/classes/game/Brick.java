package roundoid.classes.game;

public class Brick {
	
	public float fHeightWidth;
	public int iIndex;
	public float fXOffSet;
	public float fYOffSet;
	public boolean bShouldBeDrawn;
	
	
	
	public Brick()
	{
		
	}
	
	public Brick(float _fHeightWidth, int _iIndex, float _fXOffSet, float _fYOffSet,boolean _bShouldBeDrawn)
	{
		fHeightWidth = _fHeightWidth;
		iIndex = _iIndex;	
		fXOffSet = _fXOffSet;
		fYOffSet = _fYOffSet;
		bShouldBeDrawn = _bShouldBeDrawn;
	}
	
	public void Draw()
	{
		
	}
}
