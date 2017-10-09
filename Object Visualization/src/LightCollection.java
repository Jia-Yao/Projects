import java.util.ArrayList;

//****************************************************************************
//       Light source collection class
//****************************************************************************
// History :
//   Dec 1, 2014 Created by Jia Yao
//

public class LightCollection {
	public InfiniteLight in_light;
	public PointLight po_light;
	public SpotLight sp_light;
	private ColorType Ia; 
	private boolean use_ambient, use_specular, use_diffuse, use_in_light, use_po_light, use_sp_light;
	
	// constructor
	public LightCollection(InfiniteLight _in_light, PointLight _po_light, SpotLight _sp_light, ColorType _Ia){
		in_light = _in_light;
		po_light = _po_light;
		sp_light = _sp_light;
		Ia = _Ia;
		use_ambient = use_diffuse = use_specular = use_in_light = use_po_light = use_sp_light = true;			
	}
	
	// update flags
	public void updateFlags(boolean _useAmbient, boolean _useDiffuse, boolean _useSpecular, ArrayList<Integer> _onLights){
		use_ambient = _useAmbient;
		use_diffuse = _useDiffuse;
		use_specular = _useSpecular;
		use_in_light = use_po_light = use_sp_light = false;
		for (Integer i : _onLights){
			switch (i.intValue()){
			case 1:
				use_in_light = true;
				break;
			case 2:
				use_po_light = true;
				break;
			case 3:
				use_sp_light = true;
				break;
			}
		}
	}
	
	// apply the collection of light sources to the vertex (ver) / normal (n), given material properties (mat) and the viewing vector (v)
	// return resulting color value
	public ColorType applyLight(Material mat, Vector3D v, Vector3D n, Vector3D ver){
		ColorType res = new ColorType(), sum;
		double dot, dl, f_radial, f_angular, c_alpha;
		Vector3D direction, Vobj;
		// the ambient term
		if (use_ambient){
			res.r += (float)(mat.ka.r*Ia.r);
			res.g = (float)(mat.ka.g*Ia.g);
			res.b = (float)(mat.ka.b*Ia.b);
		}
		
		// the infinite light source
		if (use_in_light){
			// dot product between light direction and normal
			// light must be facing in the positive direction
			// dot <= 0.0 implies this light is facing away (not toward) this point
			// therefore, light only contributes if dot > 0.0
			direction = in_light.direction;
			direction.normalize();
			dot = direction.dotProduct(n);
			if(dot>0.0)
			{
				// diffuse component
				if(mat.diffuse && use_diffuse)
				{
					res.r += (float)(dot*mat.kd.r*in_light.color.r);
					res.g += (float)(dot*mat.kd.g*in_light.color.g);
					res.b += (float)(dot*mat.kd.b*in_light.color.b);
				}
				// specular component
				if(mat.specular && use_specular)
				{
					Vector3D r = direction.reflect(n);
					dot = r.dotProduct(v);
					if(dot>0.0)
					{
						res.r += (float)Math.pow((dot*mat.ks.r*in_light.color.r),mat.ns);
						res.g += (float)Math.pow((dot*mat.ks.g*in_light.color.g),mat.ns);
						res.b += (float)Math.pow((dot*mat.ks.b*in_light.color.b),mat.ns);
					}
				}
			}
		}
		
		// the point light source (radial attenuation)
		if (use_po_light){
			// dot product between light direction and normal
			// light must be facing in the positive direction
			// dot <= 0.0 implies this light is facing away (not toward) this point
			// therefore, light only contributes if dot > 0.0
			direction = po_light.origin.minus(ver);
			dl = direction.magnitude();
			direction.normalize();
			dot = direction.dotProduct(n);
			sum = new ColorType();
			if(dot>0.0)
			{
				// diffuse component
				if(mat.diffuse && use_diffuse)
				{
					sum.r += (float)(dot*mat.kd.r*po_light.color.r);
					sum.g += (float)(dot*mat.kd.g*po_light.color.g);
					sum.b += (float)(dot*mat.kd.b*po_light.color.b);
				}
				// specular component
				if(mat.specular && use_specular)
				{
					Vector3D r = direction.reflect(n);
					dot = r.dotProduct(v);
					if(dot>0.0)
					{
						sum.r += (float)Math.pow((dot*mat.ks.r*po_light.color.r),mat.ns);
						sum.g += (float)Math.pow((dot*mat.ks.g*po_light.color.g),mat.ns);
						sum.b += (float)Math.pow((dot*mat.ks.b*po_light.color.b),mat.ns);
					}
				}
				f_radial = 1/(po_light.a0 + po_light.a1*dl + po_light.a2*dl*dl);
				res.r += (float)f_radial*sum.r;
				res.g += (float)f_radial*sum.g;
				res.b += (float)f_radial*sum.b;
			}
		}
		
		// the directional spotlight source (angular attenuation)
		if (use_sp_light){
			// dot product between light direction and normal
			// light must be facing in the positive direction
			// dot <= 0.0 implies this light is facing away (not toward) this point
			// therefore, light only contributes if dot > 0.0
			direction = sp_light.direction;
			direction.normalize();
			dot = direction.dotProduct(n);
			sum = new ColorType();
			if(dot>0.0)
			{
				// diffuse component
				if(mat.diffuse && use_diffuse)
				{
					sum.r += (float)(dot*mat.kd.r*sp_light.color.r);
					sum.g += (float)(dot*mat.kd.g*sp_light.color.g);
					sum.b += (float)(dot*mat.kd.b*sp_light.color.b);
				}
				// specular component
				if(mat.specular && use_specular)
				{
					Vector3D r = direction.reflect(n);
					dot = r.dotProduct(v);
					if(dot>0.0)
					{
						sum.r += (float)Math.pow((dot*mat.ks.r*sp_light.color.r),mat.ns);
						sum.g += (float)Math.pow((dot*mat.ks.g*sp_light.color.g),mat.ns);
						sum.b += (float)Math.pow((dot*mat.ks.b*sp_light.color.b),mat.ns);
					}
				}
				Vobj = ver.minus(sp_light.origin);
				Vobj.normalize();
				c_alpha = direction.dotProduct(Vobj);
				if (c_alpha > Math.cos(sp_light.theta)){	// inside the cone that is lit up
					f_angular = Math.pow(c_alpha, sp_light.al);
					res.r += (float)f_angular*sum.r;
					res.g += (float)f_angular*sum.g;
					res.b += (float)f_angular*sum.b;
				}
			}
		}
		
		
			
		// clamp so that allowable maximum illumination level is not exceeded
		res.r = (float) Math.min(1.0, res.r);
		res.g = (float) Math.min(1.0, res.g);
		res.b = (float) Math.min(1.0, res.b);

		return(res);
	}
	
}
