//****************************************************************************
//       Infinite light source class
//****************************************************************************
// History :
//	 Dec 1, 2015 Modified by Jia Yao
//   Nov 6, 2014 Created by Stan Sclaroff
//
public class InfiniteLight 
{
	public Vector3D direction;
	public ColorType color;
	private Quaternion Q_inv, P;
	
	public InfiniteLight(ColorType _c, Vector3D _direction)
	{
		color = new ColorType(_c);
		direction = new Vector3D(_direction);
		
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
