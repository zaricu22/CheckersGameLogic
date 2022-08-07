
interface CheckIntrf {
		
	// Vraca trenutnog igraca
	// true - beli, false - crni
	public boolean trenutniIgrac();
	
	// Menja trenutnog igraca u suprotnog
	public void promeniIgraca();
	
	// Da li se na prosledjenoj koordinati nalazi figura trenutnog igraca
	// i dali moze da se krece tj. postoji prazno polje neposredno oko nje
	public boolean mozeKretati(int x, int y);
	
	// Proverava samo dali je prosledjeno polje figura trenutnog igraca
	// moze se modifikovati da jos vraca dali je obicna ili dama
	public boolean daliFigura(int x, int y);
	
	// Da li bilo koja figura trenutnog igraca mora uzeti neku protivnicku figuru
	// vraca listu kordinata svake takve figure
	public ArrayList<Integer> moraUzeti();
	
	// Nijedan igrac nemoze da uzima i krece u jednom potezu, ili jedno ili drugo
	// Vraca listu mogucih polja za potez 'kretanja' za figuru sa prosledjenim koordinatama
	public ArrayList<Integer> moguceKretanje(int x, int y);
	
	// Vraca listu mogucih polja za potez 'uzimanja' za figuru sa prosledjenim koordinatama
	public ArrayList<Integer> moguceUzimanje(int x, int y);
	
	// Nijedan igrac nemoze da uzima i krece u jednom potezu, ili jedno ili drugo
	// Izvrsavanje izabranog poteza, parametri: trenutno polje figure(x1,y1) i njeno ciljno polje(x2,y2)
	public void potezKretanje(int x1, int y1, int x2, int y2);
	
	// Izvrsavanje izabranog poteza, parametri: trenutno polje figure(x1,y1) i njeno ciljno polje(x2,y2)
	public void potezUzimanje(int x1, int y1, int x2, int y2);
	
}
