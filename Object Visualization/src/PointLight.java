//****************************************************************************
//       Point light source class
//****************************************************************************
// History :
//   Dec 1, 2014 Created by Jia Yao
//
public class PointLight 
{
	public Vector3D origin;
	public ColorType color;
	public float a0, a1, a2;
	
	public PointLight(ColorType _c, Vector3D _origin, float _a0, float _a1, float _a2)
	{
		color = new ColorType(_c);
		origin = new Vector3D(_origin);
		a0 = _a0;
		a1 = _a1;
		a2 = _a2;
	}
	
	public void translate (float _xOffset, float _yOffset, float _zOffset){
		origin.x += _xOffset;
		origin.y += _yOffset;
		origin.z += _zOffset;
	}
}
