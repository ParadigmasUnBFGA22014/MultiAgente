import com.util.database.daos.ArrematanteDao;
import com.util.database.pojos.Arrematante;


public class mainClass {

	public static void main(String[] args) {
		
		Arrematante ar= new Arrematante("teste", 100.0);
		ArrematanteDao ad = new ArrematanteDao();
		
		ad.addArrematante(ar);

	}

}
