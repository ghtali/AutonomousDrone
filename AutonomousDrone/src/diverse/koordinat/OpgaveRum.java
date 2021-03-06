package diverse.koordinat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import billedanalyse.Squares;
import diverse.Log;
import diverse.circleCalc.Vector2;
import diverse.koordinat.Genstand.GENSTAND_FARVE;;


/**Rettet 7/6 kl 9:30	
 * 
 * @author KimDrewess
 * Klasse der repræsenterer et OpgaveRum
 * OpgaveRum's objektet initialiseres ved at angive koordinater i cm
 * Vægmarkeringerne bliver indlæst fra wallmarks.txt (Bliver skrvet efter der indtastes i WallValues GUIen)
 *
 */
public class OpgaveRum {

	private Koordinat[][] rum;
	private Koordinat dp; // dronePosition
	private int længde = 0;
	private int bredde = 0;
	private boolean isMarkingOk = true;
	private ArrayList<Koordinat> fundneGenstande;
	private Koordinat obstacleCenter = null;
	private double yaw = -99999;
	private boolean circleFlag = false;

	private Vector2[] circleMarkings = new Vector2[2];
	private double[] circleDist =  new double[2];
	private double dist1,dist2;

	public int getLength() {
		return længde;
	}


	public int getWidth() {
		return bredde;
	}
	// Et array til at holde styr på koordinaterne til vægmarkeringerne ( Indlæses i setMarkingsmetoden)
	public Koordinat[] markingKoordinater = new Koordinat[20];	

	// markings er et array af de 200 kende vægmarkeringer, bliver tildelt en koordinat vha setMarkings
	WallMarking[] markings = {
			new WallMarking("W00.00"),new WallMarking("W00.01"),
			new WallMarking("W00.02"),new WallMarking("W00.03"),
			new WallMarking("W00.04"),new WallMarking("W01.00"),
			new WallMarking("W01.01"),new WallMarking("W01.02"),
			new WallMarking("W01.03"),new WallMarking("W01.04"),
			new WallMarking("W02.00"),new WallMarking("W02.01"),
			new WallMarking("W02.02"),new WallMarking("W02.03"),
			new WallMarking("W02.04"),new WallMarking("W03.00"),
			new WallMarking("W03.01"),new WallMarking("W03.02"),
			new WallMarking("W03.03"),new WallMarking("W03.04")
	};


	// Konstruktør tager imod længde og bredde, opretter et koordinatsystem, og sætter markeringerne
	public OpgaveRum() throws NumberFormatException, IOException {
		fundneGenstande = new ArrayList<>();
		setSize();

		rum = new Koordinat[bredde][længde];
		for (int i = 0; i < bredde; i++) {
			for (int j = 0; j < længde; j++) {
				rum[i][j] = new Koordinat(i, j);
			}
		}
		setMarkings();

		//		for (int i = 0; i < bredde; i = i+100) {
		//			for (int j = 0; j < længde; j=j+100) {
		//				addGenstandTilKoordinat(rum[i][j], new Genstand(GENSTAND_FARVE.RØD));
		//			}
		//		}
	}


	/**
	 * setSize() læser fra roomSize.txt og sætter længde og bredde af rummet.
	 * @throws NumberFormatException
	 * @throws IOException
	 */

	private void setSize() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File("roomSize.txt")));
		String size;
		if((size = br.readLine())!= null){
			bredde = Integer.parseInt(size)+1; // lægger én til
		}
		if((size = br.readLine())!= null){
			længde = Integer.parseInt(size)+1; // Lægger én til.
		}
		br.close();
	}


	/**
	 * Metode til at tilføje genstande til et koordinat
	 * @param koordinat koordinatet hvori genstanden skal lægge
	 * @param genstand angiver hvilken genstand der er tale om (bruge new Genstand())
	 */
	public void addGenstandTilKoordinat(Koordinat koordinat, Genstand genstand){
		// Tjek om genstanden er i rummets koordinatsystem	
		if(koordinat.getX() > 0 && koordinat.getX() < 963 && koordinat.getY() > 0 && koordinat.getY() < 1078){
			koordinat.addGenstand(genstand);
			if(!fundneGenstande.isEmpty()){
				boolean addObject = true;
				for(int i=0; i<fundneGenstande.size(); i++){
					if(fundneGenstande.get(i).getGenstande().getFarve().equals(genstand.getFarve())){
						if(fundneGenstande.get(i).dist(koordinat) < 40){ // Mindst 40 cm mellem hvert målobjekt af samme farve
							addObject = false;
						}
					} else if (fundneGenstande.get(i).dist(koordinat) < 5){ // Mindst 5 cm mellem hvert målobjekt af forskellig farve
						addObject = false;
					}
				}
				if(addObject){				
					fundneGenstande.add(koordinat);
					Log.writeLog(koordinat);
				}
			} else {
				fundneGenstande.add(koordinat);
				Log.writeLog("MÅLOBJEKT FUNDET! \t" + koordinat.toString() + "\t FARVE: " + genstand.getFarve());
			}
		}
	}

	/**
	 * 
	 * @param længde
	 * @param bredde
	 * @return Returnere det koordinat der ligger på det angivende længde og bredde
	 */
	public Koordinat hentKoordinat(int længde, int bredde){
		return rum[bredde][længde];
	}

	//Udskriver alle koordinaterne til Loggen
	public void udskrivKoordinater(){
		Log.writeLog("Der er fundet følgende");
		for (int i = 0; i < bredde; i++) {
			for (int j = 0; j < længde; j++) {
				Log.writeLog(rum[i][j].toString());
			}
		}
	}

	// Sætter vægmarkeringerne efter wallmarks.txt
	public void setMarkings(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("wallmarks.txt")));
			String wallmark;
			for(int i = 0; i< markings.length; i++){
				try {
					if((wallmark = br.readLine())!= null){
						String[] temp = wallmark.split(",");
						int x = Integer.parseInt(temp[0]);
						int y = Integer.parseInt(temp[1]);

						markingKoordinater[i]= new Koordinat(x, y);

					}

				} catch (Exception e) {

				}
			}


			br.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	//Bruges til at udskrive når der er fundet en genstand til loggen
	public void writeMarkingsToLog(){
		for(int i = 0; i < længde ;i++){
			for(int j = 0; j < bredde ;j++){
				try{
					if(rum[i][j].getMarking() instanceof WallMarking){
						Log.writeLog("Væg Markering fundet på :" + i+ "," +j);
					}
				}catch(NullPointerException e){

				}

			}
		}
	}

	// hjælpe metode til getMultiMarkings()
	private int getMarkeringNummer(String markName){
		for(int i = 0; i< markings.length; i++){
			if(markings[i].getString().equals(markName)){
				return i;
			}
		}
		throw new NullPointerException("Fejl");
		//		return -1;
	}


	/**
	 * 
	 * @param markName Den String som QR code scanneren returnerer
	 * @return Returnere et Array med den aflæste QR Plakat, samt positionerne af dens naboer i form af Vektor2 objekter
	 */
	public Vector2[] getMultiMarkings(String markName){


		int i = getMarkeringNummer(markName);
		Vector2 middle = markingKoordinater[i].getVector();

		Vector2 left;
		Vector2 right;

		if(i == 0){
			left =  markingKoordinater[19].getVector();
			right = markingKoordinater[1].getVector();
		}else if(i == 19){
			left = markingKoordinater[18].getVector();
			right = markingKoordinater[0].getVector();
		}else{
			left = markingKoordinater[i-1].getVector();
			right = markingKoordinater[i+1].getVector();	
		}

		Vector2[] temp = {left, middle, right};
		return temp;
	}

	public void setLength(int length){
		this.længde = længde;
	}
	public void setWidth(int width){
		this.bredde = width;
	}

	public ArrayList<Koordinat> getFoundObjects(){
		return fundneGenstande;
	}

	public void setObstacleCenter(Koordinat k){
		obstacleCenter = rum[k.getX()][k.getY()];
	}

	public boolean erForhindring(Koordinat k){

		Vector2 obstacle = obstacleCenter.getVector();
		Vector2 searchPoint = k.getVector();
		Vector2 temp = obstacle.sub(searchPoint);
		double afstand = Math.sqrt(Math.pow(temp.x, 2) + Math.pow(temp.y, 2));
		if(afstand > 80){
			return true;
		}

		return false;
	}
	public Koordinat getObstacleCenter(){
		return obstacleCenter;
	}

	public void setDronePosition(Koordinat dp, double yaw){
		this.dp = dp;
		this.yaw = yaw;
	}

	public Koordinat getDronePosition(){
		return dp;
	}

	public double getDroneYaw(){
		return yaw;
	}

	public Koordinat rotateCoordinate(Squares sqs, Koordinat drone, int height){

		int yaw = sqs.yaw;
		int x = sqs.x;
		int y = sqs.y;
		double arad = Math.toRadians(23.43);
		double brad = Math.toRadians(66.57);
		double factor = 7.7;
		factor = 960/(2*((height*Math.sin(arad))/Math.sin(brad)));

		double phi = 0;

		if(yaw > 0){
			phi = 360 - yaw;
		} else {
			phi = yaw*-1;
		}

		if (phi >= 90){
			phi-=90;
		} else {
			phi = 360 - (90 - phi);
		}
		
		if(phi%90 == 0){
			phi += 0.000000001;			
		}

		System.out.println("phi: "+phi);

		int xcenter = 640;
		int ycenter = 360;
		int xcorrect = 1;
		int ycorrect = -1;

		double xcorrected = (x-xcenter)*xcorrect;
		double ycorrected = (y-ycenter)*ycorrect;

		
		double dronex = drone.getX();
		double droney = drone.getY();
		
//		System.out.println("x in: "+xcorrected/factor);
//		System.out.println("y in: "+ycorrected/factor);
		
		double cosphi = Math.cos(Math.toRadians(phi));
		double sinphi = Math.sin(Math.toRadians(phi));

		double[][] rotmat = {
				{cosphi,sinphi,xcorrected/factor},
				{-sinphi,cosphi,ycorrected/factor}
		};

		double n = rotmat[0][0], m = rotmat[1][0];
		for(int i = 0; i < rotmat.length - 1 ; ++i) {
			for(int j = 0 ; j < rotmat[0].length ; ++j) {
				rotmat[i+1][j] = n*rotmat[i+1][j] - m*rotmat[i][j];
				rotmat[i][j] = rotmat[i][j]/n;
			}
		}

		double y2 = rotmat[1][2] / rotmat[1][1];
		double x2 = rotmat[0][2] - rotmat[0][1]*y2;

//		System.out.println("x out: "+x2);
//		System.out.println("y out: "+y2);

		double xny = x2+dronex;
		double yny = y2+droney;

//		System.out.println("x final: "+xny);
//		System.out.println("y final: "+yny);

		return new Koordinat((int)xny, (int)yny);
	}

	public void setCircleInfo(Vector2 v1, Vector2 v2, double dist1, double dist2){
		circleMarkings[0] = v1;
		circleMarkings[1] = v2;
		circleDist[0] = dist1;
		circleDist[1] = dist2;
		circleFlag = true;

	}

	public double[] getCircleDists() {
		return circleDist;
	}


	public Vector2[] getCircleCenters() {
		return circleMarkings;
	}

	public boolean isCircleFlag(){
		return circleFlag;
	}
}




