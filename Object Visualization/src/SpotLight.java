//****************************************************************************
//       Directional Spotlight source class
//****************************************************************************
// History :
//   Dec 1, 2014 Created by Jia Yao
//
public class SpotLight 
{
	public Vector3D direction;
	public Vector3D origin;
	public ColorType color;
	public float al, theta;
	private Quaternion Q_inv, P;
	
	public SpotLight(ColorType _c, Vector3D _direction, Vector3D _origin, float _al, float _theta)
	{
		color = new ColorType(_c);
		direction = new Vector3D(_direction);
		origin = new Vector3D(_origin);
		al = _al;
		theta = _theta;
	}
	
	public void translate (float _xOffset, float _yOffset, float _zOffset){
		origin.x += _xOffset;
		origin.y += _yOffset;
		origin.z += _zOffset;
	}
	
	public void rotate(Quaternion Q){
		// rotate the direction
		Q_inv = Q.conjugate();
		P = new Quaternion((float)0.0,direction); 
		P=Q.multiply(P);
		P=P.multiply(Q_inv);
		direction = P.get_v();
		direction.normalize();
	}
	
}
