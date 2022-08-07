package Matrica;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class CheckersImpl implements CheckersIntrf {
	//konstante - vrednosti polja table
	private final int CRNO = 0;
	private final int BELO = 1;
	private final int CRNAF = 2;
	private final int CRNAD = 4;
	private final int BELAF = 3;
	private final int BELAD = 5;
	
	private int[][] tabla;   //matrica
	Map<Integer, Integer> beleFigure;   // samo bele figure: koor(kljuc) i figura/dama(vrednost)
	Map<Integer, Integer> crneFigure;   // samo crne figure: koor(kljuc) i figura/dama(vrednost)

	boolean tekuciIgrac = true;   // true - beli, false - crni

	public Konacna() {
		inicijalizujTablu();
	}
	
	// Ukupan broj belih figura
	public int brojBelih() {
		return beleFigure.size();
	}
	
	
	// Ukupan broj crnih figura
	public int brojCrnih() {
		return crneFigure.size();
	}
	
	
	// Vraca trenutnog igraca
	// true - beli, false - crni
	public boolean trenutniIgrac() {
		return tekuciIgrac;
	}
	
	
	// Menja trenutnog igraca u suprotnog
	public void promeniIgraca() {
		tekuciIgrac = !tekuciIgrac;
	}
	
	
	// Proverava samo dali je prosledjeno polje figura trenutnog igraca
	// moze se modifikovati da jos vraca dali je obicna ili dama
	public boolean daliFigura(int x, int y) { 
		Map<Integer, Integer> tekorg;
		// Parametri trenutnog Igraca, izbegavanje dupliranja koda
		if (tekuciIgrac) {
			tekorg = beleFigure;
		}
		else {
			tekorg = crneFigure;
		}
		// Da li se prosledjeno polje nalazi u mapi figura trenutnog igraca
		if (tekorg.containsKey(x*10 + y)) {
			return true;
		}
		return false;
	}	
	
	
	// Da li je belo polje ili protivnicka figura, ako jeste vraca 'true' u suprotnom 'false'
    public boolean daliSuprotno(int x, int y) {
        Map<Integer, Integer> invorg;
        // Parametri trenutnog Igraca, izbegavanje dupliranja koda
        if (tekuciIgrac) {
            invorg = crneFigure;
        }
        else {
            invorg = beleFigure;
        }
        // Da li se prosledjeno polje nalazi u mapi figura protivnickog igraca ili belo polje
        if (invorg.containsKey(x*10 + y) || tabla[x][y] == BELO) {
            return true;
        }
        return false;
    }
	
    
	// Da li bilo koja figura trenutnog igraca mora uzeti neku protivnicku figuru
	// vraca listu kordinata svake takve figure
	public ArrayList<Integer> moraUzeti() {
		ArrayList<Integer> lista = new ArrayList<>();
		
		Map<Integer, Integer> tekorg;
		Map<Integer, Integer> tekinv;
		int tekf, tekd;
		// Parametri za skracivanje koda
		if (tekuciIgrac) {
			tekinv = crneFigure; // figure trenutnog igraca
			tekorg = beleFigure; // protivnicke figure
			tekf = BELAF; // obicna figura trenutnog igraca
			tekd = BELAD; // dama trenutnog igraca
		}
		else {
			tekinv = beleFigure;
			tekorg = crneFigure;;
			tekf = CRNAF;
			tekd = CRNAD;
		}
		// Proverava samo figure trenutnog igraca	
		for (Map.Entry<Integer, Integer> elem : tekorg.entrySet())
		{
			// izdvaja x i y koor svake figure
			int x = elem.getKey() / 10;
			int y = elem.getKey() % 10;
			
			// Obicna figura mora uzeti protivnicku ako se neposredno ukoso od nje nalazi protivnicka figura
			// iza koje je prazno polje, dovoljno je naci na jednoj dijagonali
			if(elem.getValue() == tekf) {
				// polje iza protivnicke mora biti u tabli
				if ((x - 2) >= 0 && (y - 2) >= 0) {
					if(tekinv.containsKey((x-1)*10 + (y-1)) && tabla[x - 2][y - 2] == CRNO) {
						lista.add(x);
						lista.add(y);
						continue;
					}
				}
				if ((x - 2) >= 0 && (y + 2) <= 9) {
					if(tekinv.containsKey((x-1)*10 + (y+1)) && tabla[x - 2][y + 2] == CRNO) {
						lista.add(x);
						lista.add(y);
						continue;
					}
				}
				if ((x + 2) <= 9 && (y - 2) >= 0) {
					if(tekinv.containsKey((x+1)*10 + (y-1)) && tabla[x + 2][y - 2] == CRNO) {
						lista.add(x);
						lista.add(y);
						continue;
					}
				}	
				if ((x + 2) <= 9 && (y + 2) <= 9) {
					if(tekinv.containsKey((x+1)*10 + (y+1)) && tabla[x + 2][y + 2] == CRNO) {
						lista.add(x);
						lista.add(y);
						continue;
					}
				}
			}
			
			// Dama mora uzeti protivnicku figuru ako se na nekoj od dijagonala prva nalazi 
			// protivnicka figura iza koje su 1/vise praznih polja, dovoljno je naci na jednoj dijagonali
			else if(elem.getValue() == tekd) {
				boolean nadjen = false;
				
				// dok je polje iza trenutnog u tabli i protivnicka figura za uzimanje nije nadjena ima se sta uzeti
				// GORNA-LEVA dijagonala
				for(int i=1; x-i-1 >= 0 && y-i-1 >= 0 && !nadjen; i++) {
					// ako naidje na figuru trenutnog igraca nema sta dalje da uzima na toj dijagonali
					if(tekorg.containsKey((x-i)*10 + (y-i)))
						break;
					// ukoliko naidje na protivnicku figuru: 1-ako iza prazno polje nadjena je protivnicka figura 
					// ako nije prazno polje nema sta dalje da se uzima
					else if(tekinv.containsKey((x-i)*10 + (y-i))){
						if(tabla[x-i-1][y-i-1] == CRNO) {
							lista.add(x);
							lista.add(y);
							nadjen = true;
						}
						else 
							break;
					}
				}
				// GORNJA-DESNA dijagonala
				for(int i=1; x-i-1 >= 0 && y+i+1 <= 9 && !nadjen; i++) {
					if(tekorg.containsKey((x-i)*10 + (y+i)))
						break;
					else if(tekinv.containsKey((x-i)*10 + (y+i))){
						if(tabla[x-i-1][y+i+1] == CRNO) {
							lista.add(x);
							lista.add(y);
							nadjen = true;
						}
						else 
							break;
					}
				}
				// DONJA-LEVA dijagonala
				for(int i=1; x+i+1 <= 9 && y-i-1 >= 0 && !nadjen; i++) {
					if(tekorg.containsKey((x+i)*10 + (y-i)))
						break;
					else if(tekinv.containsKey((x+i)*10 + (y-i))){
						if(tabla[x+i+1][y-i-1] == CRNO) {
							lista.add(x);
							lista.add(y);
							nadjen = true;
						}
						else 
							break;
					}
				}
				// DONJA-DESNA dijagonala
				for(int i=1; x+i+1 <= 9 && y+i+1 <= 9 && !nadjen; i++) {
					if(tekorg.containsKey((x+i)*10 + (y+i)))
						break;
					else if(tekinv.containsKey((x+i)*10 + (y+i))){
						if(tabla[x+i+1][y+i+1] == CRNO) {
							lista.add(x);
							lista.add(y);
							nadjen = true;
						}
						else 
							break;
					}
				}
			}
		}
		return lista;
	}
	
	
	// Nijedan igrac nemoze da uzima i krece u jednom potezu, ili jedno ili drugo
	// Vraca listu mogucih polja za potez 'kretanja' za figuru sa prosledjenim koordinatama
	public ArrayList<Integer> moguceKretanje(int x, int y) {
        ArrayList<Integer> lista = new ArrayList<>();
       
        Map<Integer, Integer> tekorg;
        int tekf, tekd;
        // Parametri za skracivanje koda
        if (tekuciIgrac) {
            tekorg = beleFigure;
            tekf = BELAF;
            tekd = BELAD;
        }
        else {
            tekorg = crneFigure;
            tekf = CRNAF;
            tekd = CRNAD;
        }
        // Ako trenutni igrac ima figuru na prosledjenim koor(x,y)
        if (tekorg.containsKey(x*10 + y)) {
            // Ako je obicna figura
            if(tekorg.get(x*10 + y) == tekf) {
                // beli
                if((x - 1) >= 0 && (y - 1) >= 0 && tabla[x-1][y-1] == CRNO && tekf == BELAF) {
                     lista.add(x-1);
                     lista.add(y-1);
                }
                if((x - 1) >= 0 && (y + 1) <= 9 && tabla[x-1][y+1] == CRNO && tekf == BELAF) {
                     lista.add(x-1);
                     lista.add(y+1);
                }
                // crni
                if((x + 1) <= 9 && (y - 1) >= 0 && tabla[x+1][y-1] == CRNO && tekf == CRNAF) {
                     lista.add(x+1);
                     lista.add(y-1);
                }
                if((x + 1) <= 9 && (y + 1) <= 9 && tabla[x+1][y+1] == CRNO && tekf == CRNAF) {                        
                     lista.add(x+1);
                     lista.add(y+1);
                }
            }
            // Ako je dama
            else if(tekorg.get(x*10 + y) == tekd) {
                // dok ima uzastopnih praznih polja na dijagonalama dodaj ih u moguce poteze
                for(int i=1; x-i >= 0 && y-i >= 0 && tabla[x-i][y-i] == CRNO; i++) {
                     lista.add(x-i);
                     lista.add(y-i);
                }
                for(int i=1; x-i >= 0 && y+i <= 9 && tabla[x-i][y+i] == CRNO; i++) {
                     lista.add(x-i);
                     lista.add(y+i);
                }
                for(int i=1; x+i <= 9 && y-i >= 0 && tabla[x+i][y-i] == CRNO; i++) {
                     lista.add(x+i);
                     lista.add(y-i);
                }
                for(int i=1; x+i <= 9 && y+i <= 9 && tabla[x+i][y+i] == CRNO; i++) {
                     lista.add(x+1);
                     lista.add(y+i);
                }
            }
        }
        return lista;
    }
	
	
	// Vraca listu mogucih polja za potez 'uzimanje' za figuru sa prosledjenim koordinatama
	public ArrayList<Integer> moguceUzimanje(int x, int y) {
		ArrayList<Integer> lista = new ArrayList<>();
		
		Map<Integer, Integer> tekorg;
		Map<Integer, Integer> tekinv;
		int tekf, tekd, invf, invd;
		// Parametri za skracivanje koda
		if (tekuciIgrac) {
			tekorg = beleFigure;
			tekinv = crneFigure;
			tekf = BELAF;
			tekd = BELAD;
			invf = CRNAF;
			invd = CRNAD;
		}
		else {
			tekorg = crneFigure;
			tekinv = beleFigure;
			tekf = CRNAF;
			tekd = CRNAD;
			invf = BELAF;
			invd = BELAD;
		}
		// Ako trenutni igrac ima figuru na prosledjenim koor(x,y)
		if (tekorg.containsKey(x*10 + y)) {
			// Ako je obicna figura
			if(tekorg.get(x*10 + y) == tekf) {
				// Polje iza protivnicke figure na dijagonali mora biti u tabli i prazno inace nema se sta uzimati
				// protivnicka figura mora se nalaziti neposredno ukoso
				if((x - 2) >= 0 && (y - 2) >= 0 &&
				(tabla[x-1][y-1] == invf || tabla[x-1][y-1] == invd) && tabla[x-2][y-2] == CRNO) {
					lista.add(x-2);
					lista.add(y-2);
				}
				if((x - 2) >= 0 && (y + 2) <= 9 &&
				(tabla[x-1][y+1] == invf || tabla[x-1][y+1] == invd) && tabla[x-2][y+2] == CRNO) {
					lista.add(x-2);
					lista.add(y+2);
				}
				if((x + 2) <= 9 && (y - 2) >= 0 &&
				(tabla[x+1][y-1] == invf || tabla[x+1][y-1] == invd) && tabla[x+2][y-2] == CRNO) {
					lista.add(x+2);
					lista.add(y-2);
				}
				if((x + 2) <= 9 && (y + 2) <= 9 &&
				(tabla[x+1][y+1] == invf || tabla[x+1][y+1] == invd) && tabla[x+2][y+2] == CRNO) {
					lista.add(x+2);
					lista.add(y+2);
				}
			}
			// Ako je dama
			else if(tekorg.get(x*10 + y) == tekd) {
				boolean nadjen = false;
				boolean moraUzeti = false;
				// GL dijagonala x-i y-i, pom DL x+j y-j, pom GD x-j y+j
				for(int i=1; (x-i) >= 0 && (y-i) >= 0 && tabla[x-i][y-i] != tekf && tabla[x-i][y-i] != tekd && !moraUzeti; i++) {
					if(nadjen) {
						for(int j=1; (x-i)+j+1 <= 9 && (y-i)-j-1 >= 0 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x-i)+j)*10 + ((y-i)-j)))
								break;
							else if(tekinv.containsKey(((x-i)+j)*10 + ((y-i)-j))){
								if(tabla[(x-i)+j+1][(y-i)-j-1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						for(int j=1; (x-i)-j-1 >= 0 && (y-i)+j+1 <= 9 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x-i)-j)*10 + ((y-i)+j)))
								break;
							else if(tekinv.containsKey(((x-i)-j)*10 + ((y-i)+j))){
								if(tabla[(x-i)-j-1][(y-i)+j+1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						// vraca prvo polje za koje dama mora opet uzeti nakon uzimanja
						if(moraUzeti) {
							lista = new ArrayList<>();
							lista.add(x-i);
							lista.add(y-i);
							return lista;
						}
						else {
							lista.add(x-i);
							lista.add(y-i);
						}
					}
					else if(tabla[x-i][y-i] == invf || tabla[x-i][y-i] == invd)
						nadjen = true;
				}
				nadjen = false;
				// GD dijagonala x-i y+j , pom GL x-j y-j, pom DD x+j y+j
				for(int i=1; x-i >= 0 && y+i <= 9 && tabla[x-i][y+i] != tekf && tabla[x-i][y+i] != tekd && !moraUzeti; i++) {
					if(nadjen) {
						for(int j=1; (x-i)-j-1 >= 0 && (y+i)-j-1 >= 0 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x-i)-j)*10 + ((y+i)-j)))
								break;
							else if(tekinv.containsKey(((x-i)-j)*10 + ((y+i)-j))){
								if(tabla[(x-i)-j-1][(y+i)-j-1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						for(int j=1; (x-i)+j+1 <= 9 && (y+i)+j+1 <= 9 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x-i)+j)*10 + ((y+i)+j)))
								break;
							else if(tekinv.containsKey(((x-i)+j)*10 + ((y+i)+j))){
								if(tabla[(x-i)+j+1][(y+i)+j+1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						if(moraUzeti) {
							lista = new ArrayList<>();
							lista.add(x-i);
							lista.add(y+i);
							return lista;
						}
						else {
							lista.add(x-i);
							lista.add(y+i);
						}
					}
					else if(tabla[x-i][y+i] == invf || tabla[x-i][y+i] == invd)
						nadjen = true;
				}
				nadjen = false;
				// DL dijagonala x+i y-j , pom GL x-j y-j, pom DD x+j y+j
				for(int i=1; x+i <= 9 && y-i >= 0 && tabla[x+i][y-i] != tekf && tabla[x+i][y-i] != tekd && !moraUzeti; i++) {
					if(nadjen) {
						for(int j=1; (x+i)-j-1 >= 0 && (y-i)-j-1 >= 0 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x+i)-j)*10 + ((y-i)-j)))
								break;
							else if(tekinv.containsKey(((x+i)-j)*10 + ((y-i)-j))){
								if(tabla[(x+i)-j-1][(y-i)-j-1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						for(int j=1; (x+i)+j+1 <= 9 && (y-i)+j+1 <= 9 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x+i)+j)*10 + ((y-i)+j)))
								break;
							else if(tekinv.containsKey(((x+i)+j)*10 + ((y-i)+j))){
								if(tabla[(x+i)+j+1][(y-i)+j+1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						if(moraUzeti) {
							lista = new ArrayList<>();
							lista.add(x+i);
							lista.add(y-i);
							return lista;
						}
						else {
							lista.add(x+i);
							lista.add(y-i);
						}
					}
					else if(tabla[x+i][y-i] == invf || tabla[x+i][y-i] == invd)
						nadjen = true;
				}
				nadjen = false;
				// DD dijagonala x+i y+j , pom GD x-j y+j, pom DL x+j y-j
				for(int i=1; x+i <= 9 && y+i <= 9 && tabla[x+i][y+i] != tekf && tabla[x+i][y+i] != tekd && !moraUzeti; i++) {
					if(nadjen) {
						for(int j=1; (x+i)-j+1 >= 0 && (y+i)+j+1 <= 9 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x+i)-j)*10 + ((y+i)+j)))
								break;
							else if(tekinv.containsKey(((x+i)-j)*10 + ((y+i)+j))){
								if(tabla[(x+i)-j-1][(y+i)+j+1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						for(int j=1; (x+i)+j+1 <= 9 && (y+i)-j-1 >= 0 && !moraUzeti; j++) {
							if(tekorg.containsKey(((x+i)+j)*10 + ((y+i)-j)))
								break;
							else if(tekinv.containsKey(((x+i)+j)*10 + ((y+i)-j))){
								if(tabla[(x+i)+j+1][(y+i)-j-1] == CRNO) {
									moraUzeti = true;
								}
								else 
									break;
							}
						}
						if(moraUzeti) {
							lista = new ArrayList<>();
							lista.add(x+i);
							lista.add(y+i);
							return lista;
						}
						else {
							lista.add(x+i);
							lista.add(y+i);
						}
					}
					else if(tabla[x+i][y+i] == invf || tabla[x+i][y+i] == invd)
						nadjen = true;
				}
			}
		}
		return lista;
	}
	
	
	// Nijedan igrac ne moze da uzima i krece u jednom potezu, ili jedno ili drugo
	// Izvrsavanje izabranog poteza, parametri: trenutno polje figure(x1,y1) i njeno ciljno polje(x2,y2)
	public void potezKretanje(int x1, int y1, int x2, int y2){
		Map<Integer, Integer> tekorg;
		int tekf, tekd, invx;
		// Parametri za skracivanje koda
		if (tekuciIgrac) {
			tekorg = beleFigure;
			tekf = BELAF;
			tekd = BELAD;
			invx = 0;
		}
		else {
			tekorg = crneFigure;
			tekf = CRNAF;
			tekd = CRNAD;
			invx = 9;
		}
		// Ako trenutni igrac ima figuru na prosledjenim koor(x1,y1) 
		if (tekorg.containsKey(x1*10 + y1)) {
			int xi = -1,yj = -1;
			// Ako je obicna figura
			if(tekorg.get(x1*10 + y1) == tekf) {
				// Odredjivanje prvog polja dijagonale u kojoj je ciljno polje
				if(x1>x2 && y1>y2) { xi = x1-1; yj = y1-1; }// x-i y-j GL	
				if(x1>x2 && y1<y2) { xi = x1-1; yj = y1+1; }// x-i y+j GD
				if(x1<x2 && y1>y2) { xi = x1+1; yj = y1-1; }// x+i y-j DL
				if(x1<x2 && y1<y2) { xi = x1+1; yj = y1+1; }// x+i y+j DD
				if(xi != -1 && yj != -1) {
					// na staro mesto figure uvek ostaje prazno crno polje
					tabla[x1][y1] = CRNO;
					// pomeri i promovisi u damu ako je figura u poslednjem protivnickom redu i nebi imala sta uzeti
					if(x2 == invx && !potencijalnoUzimanje(true, x2,y2)) {
						tabla[x2][y2] = tekd;
						tekorg.remove(x1*10+y1);
						tekorg.put(x2*10+y2, tekd);
					}
					// samo pomeri figuru na ciljno polje
					else {
						tabla[x2][y2] = tekf;
						tekorg.remove(x1*10+y1);
						tekorg.put(x2*10+y2, tekf);
					}
				}
			}
			// Ako je Dama	
			else if(tekorg.get(x1*10 + y1) == tekd) {
				// Odredjivanje prvog polja dijagonale u kojoj je ciljno polje
				if(x1>x2 && y1>y2) { xi = x1-1; yj = y1-1;  } // x-i y-j GL	
				if(x1>x2 && y1<y2) { xi = x1-1; yj = y1+1;  } // x-i y+j GD
				if(x1<x2 && y1>y2) { xi = x1+1; yj = y1-1;  } // x+i y-j DL
				if(x1<x2 && y1<y2) { xi = x1+1; yj = y1+1;  } // x+i y+j DD
				if(xi != -1 && yj != -1) {
					tabla[x1][y1] = CRNO;
					tabla[x2][y2] = tekd;
					tekorg.remove(x1*10+y1);
					tekorg.put(x2*10+y2, tekd);
				}
			}
		}
	}

	
	// Izvrsavanje izabranog poteza, parametri: trenutno polje figure(x1,y1) i njeno ciljno polje(x2,y2)
	public void potezUzimanje(int x1, int y1, int x2, int y2){
		Map<Integer, Integer> tekorg;
		Map<Integer, Integer> tekinv;
		int tekf, tekd, invf, invd, invx;
		// Parametri za skracivanje koda
		if (tekuciIgrac) {
			tekorg = beleFigure;
			tekinv = crneFigure;
			tekf = BELAF;
			tekd = BELAD;
			invf = CRNAF;
			invd = CRNAD;
			invx = 0;
		}
		else {
			tekorg = crneFigure;
			tekinv = beleFigure;
			tekf = CRNAF;
			tekd = CRNAD;
			invf = BELAF;
			invd = BELAD;
			invx = 9;
		}
		// Ako trenutni igrac ima figuru na prosledjenim koor(x1,y1) 
		if (tekorg.containsKey(x1*10 + y1)) {
			int xi = -1,yj = -1,z = 0,w = 0;
			// Ako je obicna figura
			if(tekorg.get(x1*10 + y1) == tekf) {
				// Odredjivanje prvog polja dijagonale u kojoj je ciljno polje
				if(x1>x2 && y1>y2) { xi = x1-1; yj = y1-1; }// x-i y-j GL	
				if(x1>x2 && y1<y2) { xi = x1-1; yj = y1+1; }// x-i y+j GD
				if(x1<x2 && y1>y2) { xi = x1+1; yj = y1-1; }// x+i y-j DL
				if(x1<x2 && y1<y2) { xi = x1+1; yj = y1+1; }// x+i y+j DD
				if(xi != -1 && yj != -1) {
					tabla[x1][y1] = CRNO;
					tabla[xi][yj] = CRNO;
					tekinv.remove(xi*10+yj);
					// promovisi u damu ako bi figura nakon uzimanja bila u poslednjem protivnickom redu i nebi imala sta uzeti
					if(x2 == invx && !potencijalnoUzimanje(true, x2,y2)) {
						tabla[x2][y2] = tekd;
						tekorg.remove(x1*10+y1);
						tekorg.put(x2*10+y2, tekd);
					}
					// samo pomeri
					else {
						tabla[x2][y2] = tekf;
						tekorg.remove(x1*10+y1);
						tekorg.put(x2*10+y2, tekf);
					}
				}
			}
			// Ako je Dama	
			else if(tekorg.get(x1*10 + y1) == tekd) {
				// Odredjivanje prvog polja dijagonale u kojoj je ciljno polje
				if(x1>x2 && y1>y2) { xi = x1-1; yj = y1-1; z = 1; w = 1;   } // x-i y-j GL	
				if(x1>x2 && y1<y2) { xi = x1-1; yj = y1+1; z = 1; w = -1;  } // x-i y+j GD
				if(x1<x2 && y1>y2) { xi = x1+1; yj = y1-1; z = -1; w = 1;  } // x+i y-j DL
				if(x1<x2 && y1<y2) { xi = x1+1; yj = y1+1; z = -1; w = -1; } // x+i y+j DD
				if(xi != -1 && yj != -1) {
					tabla[x1][y1] = CRNO;
					tabla[x2][y2] = tekd;
					tekorg.remove(x1*10+y1);
					tekorg.put(x2*10+y2, tekd);
					//obrisemo protivnika
					boolean nadjen = false;
					// prvo polje na dijagonali(xi,yj), dok je rastojanje polja na dijagonali manje od predposlednjeg na dijagonali
					// i nije nadjena protivnicka figura
					int j,k; //koor protivnicke figure
					for(j = xi , k = yj; Math.abs(x1-j) < Math.abs(x1-x2) && Math.abs(y1-k) < Math.abs(y1-y2)
							&& !nadjen; j = j-z, k = k-w) {
						if(tabla[j][k] == invf || tabla[j][k] == invd) 
							nadjen = true;
					}
					if(nadjen) {
					tabla[j+z][k+w] = CRNO;
					tekinv.remove(j*10+k);
					}
				}
			}
		}
	}
	
	// POMOCNA METODA ZA potezKretanje() i potezUzimanje()  
	// Da li figura(obicna - true/dama - false) na prosledjenim kordinatama mora uzeti neku protivnicku figuru
	private boolean potencijalnoUzimanje(boolean b, int x, int y) {
			Map<Integer, Integer> tekorg;
			Map<Integer, Integer> tekinv;
			// Parametri za skracivanje koda
			if (tekuciIgrac) {
				tekinv = crneFigure;
				tekorg = beleFigure;

			}
			else {
				tekinv = beleFigure;
				tekorg = crneFigure;

			}
			// Obicna figura
			if(b) {
				if ((x - 2) >= 0 && (y - 2) >= 0) {
					if(tekinv.containsKey((x-1)*10 + (y-1)) && tabla[x - 2][y - 2] == CRNO) {
						return true;
					}
				}
				if ((x - 2) >= 0 && (y + 2) <= 9) {
					if(tekinv.containsKey((x-1)*10 + (y+1)) && tabla[x - 2][y + 2] == CRNO) {
						return true;
					}
				}
				if ((x + 2) <= 9 && (y - 2) >= 0) {
					if(tekinv.containsKey((x+1)*10 + (y-1)) && tabla[x + 2][y - 2] == CRNO) {
						return true;
					}
				}	
				if ((x + 2) <= 9 && (y + 2) <= 9) {
					if(tekinv.containsKey((x+1)*10 + (y+1)) && tabla[x + 2][y + 2] == CRNO) {
						return true;
					}
				}
			}
			// Dama
			else {
				for(int i=1; x-i-1 >= 0 && y-i-1 >= 0; i++) {
					if(tekorg.containsKey((x-i)*10 + (y-i)))
						break;
					else if(tekinv.containsKey((x-i)*10 + (y-i))){
						if(tabla[x-i-1][y-i-1] == CRNO) {
							return true;
						}
						else 
							break;
					}
				}
				for(int i=1; x-i-1 >= 0 && y+i+1 <= 9; i++) {
					if(tekorg.containsKey((x-i)*10 + (y+i)))
						break;
					else if(tekinv.containsKey((x-i)*10 + (y+i))){
						if(tabla[x-i-1][y+i+1] == CRNO) {
							return true;
						}
						else 
							break;
					}
				}
				for(int i=1; x+i+1 <= 9 && y-i-1 >= 0; i++) {
					if(tekorg.containsKey((x+i)*10 + (y-i)))
						break;
					else if(tekinv.containsKey((x+i)*10 + (y-i))){
						if(tabla[x+i+1][y-i-1] == CRNO) {
							return true;
						}
						else 
							break;
					}
				}
				for(int i=1; x+i+1 <= 9 && y+i+1 <= 9; i++) {
					if(tekorg.containsKey((x+i)*10 + (y+i)))
						break;
					else if(tekinv.containsKey((x+i)*10 + (y+i))){
						if(tabla[x+i+1][y+i+1] == CRNO) {
							return true;
						}
						else 
							break;
					}
				}
			}
			return false;
		}
	
	
	private void inicijalizujTablu() {
		tabla = new int[10][10];
		beleFigure = new HashMap<Integer, Integer>();
		crneFigure = new HashMap<Integer, Integer>();
		for (int i = 0; i <= 4; i++) {
			for (int j = 0; j < 10; j++) {
				if (i < 4) {
					if (i % 2 == 0) {
						if (j % 2 == 0) {
							tabla[i][j] = BELO;
							tabla[i+6][j] = BELO;
						}
						else {
							tabla[i][j] = CRNAF;
							crneFigure.put(i*10 + j, CRNAF);
							tabla[i+6][j] = BELAF;
							beleFigure.put((i+6)*10 + j, BELAF);
						}
					} else {
						if (j % 2 == 1) {
							tabla[i][j] = BELO;
							tabla[i+6][j] = BELO;
						}
						else {
							tabla[i][j] = CRNAF;
							crneFigure.put(i*10 + j, CRNAF);
							tabla[i+6][j] = BELAF;
							beleFigure.put((i+6)*10 + j, BELAF);
						}
					}
				} else {
					if (j % 2 == 0) {
						tabla[i][j] = BELO;
						tabla[i+1][j] = CRNO;
					}
					else {
						tabla[i][j] = CRNO;
						tabla[i+1][j] = BELO;
					}
				}
			}
		}
	}
	
	//metod koji proverava da li se beli igrac moze kretati/jesti
	public boolean beliMozeKretati()
	{
		List<Integer> listaBelih = new ArrayList<Integer>(beleFigure.keySet());
		int x,y;
		ArrayList<Integer> listaMogucihPoteza;
		ArrayList<Integer> listaMogucihJedenja;
		for(Integer i : listaBelih)
		{
			x=i/10;
			y=i%10;
			listaMogucihPoteza = this.moguceKretanje(x, y);
			listaMogucihJedenja = this.moguceUzimanje(x, y);
			if(listaMogucihPoteza.size()>0)
			{
				return true;
			}
			if(listaMogucihJedenja.size()>0)
			{
				return true;
			}
			
		}
		return false;
	}
	
	
	//Metod koji proverava da li se crni igrac moze kretati/jesti
	public boolean crniMozeKretati()
	{
		List<Integer> listaCrnih = new ArrayList<Integer>(crneFigure.keySet());
		int x,y;
		ArrayList<Integer> listaMogucihPoteza;
		ArrayList<Integer> listaMogucihJedenja;
		for(Integer i : listaCrnih)
		{
			x=i/10;
			y=i%10;
			listaMogucihPoteza = this.moguceKretanje(x, y);
			listaMogucihJedenja = this.moguceUzimanje(x, y);
			if(listaMogucihPoteza.size()>0)
			{
				return true;
			}
			if(listaMogucihJedenja.size()>0)
			{
				return true;
			}
			
		}
		return false;
	}
}
