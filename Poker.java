import java.util.Scanner;

public class Poker {


	//jaaaaaa!!! Izracun binoma zdaj tudi za ogromne stevilke s to metodo deluje neverjetno hitro.

	//dal sem za 1.000.000 pa je ven prišlo takoj, ampak rezultati so bili pa kar v tisočih. Za 100.000 in za 10.000 isto.
	//zgleda se nekje zgodi overflow formata številk in ni več tako majhnih številk, zato postanejo negativne in potem
	//vedno večje zapovrh tega, da so negativne

	//pri 1500 se zdijo še v redu rezultati, pri 1600 pa se že začnejo kazati negativne številke
	//pri 1552 še ni negativnih števil, pri 1553 pa so že

	static long factorialOdMaxDoMin (int max, int min) {
		if (min >= max) {
			return 1;
		} else {
			return factorialOdMaxDoMin(max-1, min) * max;
		}
	}

	static long binomi(int n, int k) {
		//tukaj bo n-k vedno veliko vecji od k, zato sem ga izbaral za min
        return factorialOdMaxDoMin(n, n-k) / factorialOdMaxDoMin(k, 0);

    }

	// static long binomi(int n, int k) {
    //     if ((n == k) || (k == 0))
    //         return 1;
    //     else
    //         return binomi(n - 1, k) + binomi(n - 1, k - 1);
    // }

	static double royalFlush (int stStevil) {
		//ker ce je stevil manj kot 5, potem ne mores sestaviti straighta.
		if (stStevil < 5) {
			return 0;
		} else {
			return (double)4 / binomi(stStevil * 4, 5);
		}
	}

	static double straightFlush (int stStevil) {
		//isto kot pti royalFlush, ampak da je tu se ena vec, ker ce je samo ena opcija je to najvisja zadeva in je royal flush.
		if (stStevil < 6) {
			return 0;
		} else {
			//-4, ker ce gledas najvisjo karto v vseh moznih straightih je vedno stiri manj, kot je vseh kart.
			//-1 je, ker ne stejemo royal flusha
			//+1 je zato, ker je as lahko tudi na podnu
			//aja btw torej assumamo tudi, da se as nazadnje odstrani, in da gre lahko vedno na poden lestvice
			return (double) ((stStevil - 4 - 1 + 1) * 4) / binomi(stStevil * 4, 5);
			//Tu bi imeli problem, ker ko je stKart 5 je problem, da as ne more na drugo stran in je drugacna enacba.
			//a ker je tu ta primer ze odstranjen, je to to.
		}
	}

	static double fourOfAKind (int stStevil) {
		//ker ce je samo 1, potem niti ene roke ne mores izpolniti. Pa tudi ce bi hotel izpustiti, ne mores,
		//ker rabis ta peto karto za izracun vseh kombinacij, in ce te karte ni, potem preprosto sploh ni kombinacij,
		//kar enacba tudi pove.
		if (stStevil <= 1) {
			return 0;
		} else {
			//stStevil je zato, ker najprej izberes eno od stevil in to doloci 4 karte tega handa (vse bodo morale biti iste)
			//-1 je, ker tega znaka ne mores izbrati. *4 pa potem se zato, ker je vse skupaj toliko preostalih kart,
			//ki so se lahko kot tista dodatna karta v handu
			return (double) stStevil * ((stStevil-1) * 4) / binomi(stStevil * 4, 5);
		}
	}

	static double fullHouse (int stStevil) {
		//ker ce je samo 1, potem ne mores imeti dveh razlicnih stevil. Pa tudi niti enega handa ne mores izpolniti.
		if (stStevil <= 1) {
			return 0;
		} else {
			//najprej izberes eno stevilo in tega bos vzel 3 od 4. Potem pa izberes eno od
			//preostalih stevil in ga bos vzel 2 od 4.
			//Lahko bi gledal tudi obraten vrstni red vzemanja, pa bi bilo isto, ker je to mnozenje
			return (double) (stStevil * binomi(4, 3) * (stStevil-1) * binomi (4, 2)) / binomi(stStevil * 4, 5);
		}
	}

	static double flush (int stStevil) {
		//ker ce je manj kot 5, potem ne mores imeti 5 razlicnih cifer za flush.
		if (stStevil < 5) {
			return 0;
		} else {
			
			if (stStevil != 5) {
				//najprej izberes eno barvo, potem pa v tej barvi se vzames 5 od stStevil.
				//potem pa se odstejemo kombinacije royal in straight flusha, ki jih je preprosto (stKart-4+1) * 4,
				//ker je toliko razlicnih straightov krat toliko barv
				return ((double) (4 * binomi(stStevil, 5) - (stStevil - 4 + 1) * 4) / binomi(stStevil * 4, 5));
			} else {
				//ni +1, ker v tem primeru as ne more na vsako od strani in zato pri straight flushu ni tega +1.
				//Tu je skrita odvisnost od straight flusha, saj tu izvedem odbitek kombinacij straight flushov,
				//čeprav v tem primeru straight flusha sploh ni, kar se vidi iz tega, da funkcija straightFlush vrne 0.
				return ((double) (4 * binomi(stStevil, 5) - (stStevil - 4) * 4) / binomi(stStevil * 4, 5));
			}
		}
	}

	static double straight (int stStevil) {
		//ker ce je manj kot 5, ne mores imeti 5 zaporednih. (Tu smo btw predpostavili, da ko odstranjujes karte ne pustis lukenj vmes.)
		if (stStevil < 5) {
			return 0;
		} else {
			//-4, ker ce gledas najvisjo karto straighta vidis, da bo ravno toliko razlicnih straightov.
			//+1 je zato, ker je as lahko tudi na podnu
			//potem pa v tem straightu se za vsako karto izberes eno od 4 barv
			//potem pa se odstejemo kombinacije royal in straight flusha, ki jih je preprosto (stKart-4+1) * 4,
			//ker je toliko razlicnih straightov krat toliko barv
			if (stStevil != 5) {
				return (double) ((stStevil - 4 + 1) * 4 * 4 * 4 * 4 * 4  - (stStevil - 4 + 1) * 4) / binomi(stStevil * 4, 5);
			} else {
				//ni+1 in pri straightu, ker as ne more na drugo stran, in pa ni +1 pri odstevanju straight flusha,
				//ker tam tudi ne more biti tega pojava.
				return (double) ((stStevil - 4) * 4 * 4 * 4 * 4 * 4  - (stStevil - 4) * 4) / binomi(stStevil * 4, 5);
			}

		}
	}

	static double threeOfAKind (int stStevil) {
		//mora biti vsaj tri, ker rabis vsaj 3 razlicne cifre, ker sicer bi bil full house.
		if (stStevil < 3) {
			return 0;
		} else {
			//vzames eno od stevil. Potem vzames 3 od 4 moznih kart od te cifre.
			//potem pa od vseh preostalih cifer vzames 2 in pri teh cifrah 1 od 4 kart.
			return (double) stStevil * binomi(4, 3) * binomi((stStevil-1), 2) * 4 * 4 / binomi(stStevil * 4, 5);
		}
	}

	static double twoPair (int stStevil) {
		//ker ce je manj kot 3, potem ne mores imeti dveh cifer za vsakega od parov in se dodatne cifre
		if (stStevil < 3) {
			return 0;
		} else {
			//vzames kombinacijo dveh stevil. Potem vzames 2 od 4 moznih kart za vsako od teh dveh stevil.
			//Potem pa od kart z ostalimi ciframi vzames eno. 
			return (double) binomi(stStevil, 2) * binomi(4, 2) * binomi(4,2) * binomi((stStevil-2) * 4, 1)  / binomi(stStevil * 4, 5);
		}
	}

	static double pair (int stStevil) {
		//ker ce je manj kot 4, ne mores imeti 4 cifer, ki jih rabis, da je zadeva samo par. Ker sicer bo ze dva para ali pa full house.
		if (stStevil < 4) {
			return 0;
		} else {
			//vzames eno cifro, potem pa vsames 3 od 4 kart te cifre.
			//potem pa ne mores samo vzeti 3 od ostalih kart, ker bi lahko vmes bil par
			//in bi potem imel 2 para.
			//Zato vzames 3 od ostalih cifer. Potem pa za vsako cifro se vzames 1 od 4 kart,
			//ki so lahko ta cifra.
			return (double) stStevil * binomi(4, 2) * binomi(stStevil-1, 3) * 4 * 4 * 4  / binomi(stStevil * 4, 5);
		}
	}

	static double nothing (int stStevil) {
		//odbijes vse ostale
		return (double) 1 - royalFlush(stStevil) - straightFlush(stStevil) - fourOfAKind(stStevil) - fullHouse(stStevil) - flush(stStevil) - straight(stStevil) - threeOfAKind(stStevil) - twoPair(stStevil) - pair(stStevil);
	}


	static double visokParBrezDodatneVisoke (int stStevil) {
		//mora biti vsaj 4, da ni dodatnega para
		if (stStevil < 4) {
			return 0;
		} else {
			//izberes eno od visokih kart, kar je 1 cetrtina najvisjih kart
			//potem izberes 2 od 4 barv za to cifro
			//potem pa se od preostanka cifer izberes 3 in za vsako izberes barvo
			return (double) (stStevil / 4) * binomi(4, 2) * binomi((stStevil - (stStevil / 4)), 3) * 4 * 4 * 4 / binomi(stStevil * 4, 5);
		}
	}

	static double srednjiParBrezDodatneVisoke (int stStevil) {
		//mora biti vsaj 5, da ni dodatnega para in ker visoka ne more bit zraven ampak mora biti odvec
		if (stStevil < 5) {
			return 0;
		} else {
			//izberes eno od srednjih kart, kar je druga cetrtina najvisjih kart
			//potem izberes 2 od 4 barv za to cifro
			//potem pa se od nevisokih cifer izberes 3 in za vsako izberes barvo, ampak ene cifre pa ni, ker je ze par
			return (double) (stStevil / 4) * binomi(4, 2) * binomi((stStevil - (stStevil / 4)) - 1, 3) * 4 * 4 * 4 / binomi(stStevil * 4, 5);
		}
	}

	static double nizkiParBrezDodatneVisoke (int stStevil) {
		//mora biti vsaj 5, da ni dodatnega para in ker visoka ne more bit zraven ampak mora biti odvec
		if (stStevil < 5) {
			return 0;
		} else {
			//to je navzgor zaokrozena polovica stevil
			int stevilo = stStevil / 2;
			if (stStevil % 2 == 1) {
				stevilo++;
			}
			//izberes eno od nizkih kart, kar je spodnja polovica najvisjih kart, na zgor zaokrozena.
			//potem izberes 2 od 4 barv za to cifro
			//potem pa se od nevisokih cifer izberes 3 in za vsako izberes barvo, ampak ene cifre pa ni, ker je ze par
			return (double) stevilo * binomi(4, 2) * binomi((stStevil - (stStevil / 4)) - 1, 3) * 4 * 4 * 4 / binomi(stStevil * 4, 5);
		}
	}

	static double visokParZEnoDodatnoVisoko (int stStevil) {
		//mora biti vsaj 8, da sta lahko sploh 2 visoki karti
		if (stStevil < 8) {
			return 0;
		} else {
			//izberes eno od visokih kart, kar je 1 cetrtina najvisjih kart
			//potem izberes 2 od 4 barv za to cifro
			//potem od ostalih visokih kart izberes eno in njeno barvo
			//potem izberes se 2 nevisoki karti in za vsako barvo
			return (double) (stStevil / 4) * binomi(4, 2) * binomi((stStevil / 4) - 1, 1) * 4 * binomi((stStevil - (stStevil / 4)), 2) * 4 * 4 / binomi(stStevil * 4, 5);
		}
	}

	static double srednjiParZEnoDodatnoVisoko (int stStevil) {
		//mora biti vsaj 4, da ni dodatnega para
		if (stStevil < 4) {
			return 0;
		} else {
			//izberes eno od srednjih kart, kar je druga cetrtina najvisjih kart
			//potem izberes 2 od 4 barv za to cifro
			//potem izberes eno od visokih kart in izberes njeno barvo
			//potem pa izbereš
			//potem pa se od nevisokih cifer izberes 2 in za vsako izberes barvo, ampak ene cifre pa ni, ker je ze par
			return (double) (stStevil / 4) * binomi(4, 2) * binomi(stStevil / 4, 1) * 4 * binomi((stStevil - (stStevil / 4)) - 1, 2) * 4 * 4 / binomi(stStevil * 4, 5);
		}
	}

	static double nizkiParZEnoDodatnoVisoko (int stStevil) {
		//mora biti vsaj 4, da ni dodatnega para
		if (stStevil < 4) {
			return 0;
		} else {
			//to je navzgor zaokrozena polovica stevil
			int stevilo = stStevil / 2;
			if (stStevil % 2 == 1) {
				stevilo++;
			}
			//izberes eno od nizkih kart, kar je spodnja polovica najvisjih kart, na zgor zaokrozena.
			//potem izberes 2 od 4 barv za to cifro
			//potem izbers eno od visokih in njeno barvo
			//potem izberes dve od nevisokih - 1 za to, ki je ze par, pa se izberes jima barvo
			return (double) stevilo * binomi(4, 2) * binomi(stStevil / 4, 1) * 4 * binomi((stStevil - (stStevil / 4)) - 1, 2) * 4 * 4 / binomi(stStevil * 4, 5);
		}
	}

	static double sestevekProcentovParovBrezInZEnoVisoko (int stStevil) {
		
		return visokParBrezDodatneVisoke(stStevil) + visokParZEnoDodatnoVisoko(stStevil) + srednjiParBrezDodatneVisoke(stStevil) + srednjiParZEnoDodatnoVisoko(stStevil) + nizkiParBrezDodatneVisoke(stStevil) + nizkiParZEnoDodatnoVisoko(stStevil);
		
	}

	static double procentovParaZVecVisokimi (int stStevil) {
		
		return pair(stStevil) - (visokParBrezDodatneVisoke(stStevil) + visokParZEnoDodatnoVisoko(stStevil) + srednjiParBrezDodatneVisoke(stStevil) + srednjiParZEnoDodatnoVisoko(stStevil) + nizkiParBrezDodatneVisoke(stStevil) + nizkiParZEnoDodatnoVisoko(stStevil));
		
	}

	static double flushDrawBrezParovAPustimoStraighte (int stStevil) {
		//mora biti vsaj 5, da ni para
		if (stStevil < 5) {
			return 0;
		} else {
			
			//izberes 4 od stStevil, potem pa izberes v kateri od stirih barv je to
			//izberes eno od ostalih cifer, in izberes eno od ostalih barv
			return (double) binomi(stStevil, 4) * 4 * (stStevil - 4) * 3 / binomi(stStevil * 4, 5);
		}
	}

	//tole je labavo, ker ne uposteva, da je as lahko na podnu. Ampak res se mi ne da se s tem ukvarjat,
	//ker ni velika razlika, in ker je to dokaj nepomembna statistika
	static double doubleEndedStraightDrawBrezParovInBrezFlushov (int stStevil) {
		//mora biti vsaj 7, je lahko 6 zaporednih in se ena nekje
		if (stStevil < 6) {
			return 0;
		} else {
			
			//izberes eno od zaporedij 6 zaporednih. gledas glede na najvisjo karto zaporedja
			//izberes eno od 4 barv za vsako od stirih kart
			//izberes eno od ostalih cifer
			//izberes barvo zanjo
			return (double) (stStevil - 5) * 4 * 4 * 4 * 4 * (stStevil - 6) * 4 / binomi(stStevil * 4, 5);
		}
	}






	// Izracun mosnosti, da nihce nima neke kombinacije.
	// Za razlago si poglej zapiske o matematicnem approachu do 5 card stud pokra

	static double potenca (double osnova, long potenca) {
		if (potenca < 1) {
			return 0;
		} else {
			double zmnozek = 1;
			for (long i = 0; i < potenca; i++) {
				zmnozek *= osnova;
			}
			return zmnozek;
		}
	}

	// moznost da vsaj nekdo od toliko igralcev ima to kombinacijo

	static double nekdoRoyalFlush (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = royalFlush(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoStraightFlush (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = straightFlush(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoFourOfAKind (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = fourOfAKind(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoFullHouse (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = fullHouse(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoFlush (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = flush(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoStraight (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = straight(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoThreeOfAKind (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = threeOfAKind(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoTwoPair (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = twoPair(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoPair (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = pair(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoVisokPairZVisoko (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = visokParZEnoDodatnoVisoko(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoVisokPairBrezVisoke (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = visokParBrezDodatneVisoke(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoSrednjiParZVisoko (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = srednjiParZEnoDodatnoVisoko(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoSrednjiParBrezVisoke (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = srednjiParBrezDodatneVisoke(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoNizekParZVisoko (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = nizkiParZEnoDodatnoVisoko(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	static double nekdoNizekParBrezVisoke (int stStevil, int stIgralcev) {

		double moznostEnegaIgralca = nizkiParBrezDodatneVisoke(stStevil);
		double moznostDaEnIgralecNima = 1 - moznostEnegaIgralca;
		double nihceNima = potenca(moznostDaEnIgralecNima, stIgralcev);
		double vsajNekdoIma = 1 - nihceNima;

		return vsajNekdoIma;
	}

	// Tu notri bom lahko pisal vsa bodoča izpisovanja, ki morajo po nekem algoritmu vzemati procente ostalih stvari.
	static void izpisovalnaFunkcija (int stStevil, int stIgralcev) {

		int stKombinacij = 14;

		double[] moznostiZadev = new double[20];
		moznostiZadev[0] = royalFlush(stStevil);
		moznostiZadev[1] = straightFlush(stStevil);
		moznostiZadev[2] = fourOfAKind(stStevil);
		moznostiZadev[3] = fullHouse(stStevil);
		moznostiZadev[4] = flush(stStevil);
		moznostiZadev[5] = straight(stStevil);
		moznostiZadev[6] = threeOfAKind(stStevil);
		moznostiZadev[7] = twoPair(stStevil);
		moznostiZadev[8] = visokParZEnoDodatnoVisoko(stStevil);
		moznostiZadev[9] = visokParBrezDodatneVisoke(stStevil);
		moznostiZadev[10] = srednjiParZEnoDodatnoVisoko(stStevil);
		moznostiZadev[11] = srednjiParBrezDodatneVisoke(stStevil);
		moznostiZadev[12] = nizkiParZEnoDodatnoVisoko(stStevil);
		moznostiZadev[13] = nizkiParBrezDodatneVisoke(stStevil);

		double[] kumulativneDoSedaj = new double[20];

		kumulativneDoSedaj[0] = moznostiZadev[0];
		for (int i = 1; i < stKombinacij; i++) {
			kumulativneDoSedaj[i] = kumulativneDoSedaj[i-1] + moznostiZadev[i];
		}


		System.out.println("Kumulativne moznosti od najvisje kombinacije do zdajsnje:");
		for (int i = 0; i < stKombinacij; i++) {
			if(i == 4)
				System.out.println("Flush:");
			if(i == 8)
				System.out.println("Pari:");

			System.out.printf("%.9f\n", kumulativneDoSedaj[i] * 100);
		}

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

		double[] moznostDaNihceNima = new double[20];







		//tole je poskus kumulativne moznosti, da vsaj nekdo ima. Ampak je napacna metoda, ker ne mores kar sestevat.


		// double[] nekdoIma = new double[20];
		// nekdoIma[0] = nekdoRoyalFlush(stStevil, stIgralcev);
		// nekdoIma[1] = nekdoStraightFlush(stStevil, stIgralcev);
		// nekdoIma[2] = nekdoFourOfAKind(stStevil, stIgralcev);
		// nekdoIma[3] = nekdoFullHouse(stStevil, stIgralcev);
		// nekdoIma[4] = nekdoFlush(stStevil, stIgralcev);
		// nekdoIma[5] = nekdoStraight(stStevil, stIgralcev);
		// nekdoIma[6] = nekdoThreeOfAKind(stStevil, stIgralcev);
		// nekdoIma[7] = nekdoTwoPair(stStevil, stIgralcev);
		// nekdoIma[8] = nekdoVisokPairZVisoko(stStevil, stIgralcev);
		// nekdoIma[9] = nekdoVisokPairBrezVisoke(stStevil, stIgralcev);
		// nekdoIma[10] = nekdoSrednjiParZVisoko(stStevil, stIgralcev);
		// nekdoIma[11] = nekdoSrednjiParBrezVisoke(stStevil, stIgralcev);
		// nekdoIma[12] = nekdoNizekParZVisoko(stStevil, stIgralcev);
		// nekdoIma[13] = nekdoNizekParBrezVisoke(stStevil, stIgralcev);

		// double[] nekdoImaKumulativno = new double[20];

		// nekdoImaKumulativno[0] = nekdoIma[0];
		// for (int i = 1; i < stKombinacij; i++) {
		// 	nekdoImaKumulativno[i] = nekdoImaKumulativno[i-1] + nekdoIma[i];
		// }

		// for (int i = 0; i < stKombinacij; i++) {
		// 	if(i == 4)
		// 		System.out.println("Flush:");
		// 	if(i == 8)
		// 		System.out.println("Pari:");

		// 	System.out.printf("%.2f\n", nekdoImaKumulativno[i] * 100);
		// }



	}
	







	public static void main (String [] args) {

		Scanner sc = new Scanner(System.in);

		System.out.print("Vpisite stevilo stevil in znakov v kupcku: ");
		int stZnakov = sc.nextInt();
		System.out.println();

		System.out.print("Vpisite stevilo ostalih igralcev: ");
		int stOstalihIgralcev = sc.nextInt();
		System.out.println();

		System.out.println("Moznosti v procentih, da v roki 5 kart pade neka zadeva:\n");

		System.out.println("Royal flush:");
		System.out.printf("%.2f\n", royalFlush(stZnakov) * 100);
		System.out.println();

		System.out.println("Straight flush:");
		System.out.printf("%.2f\n", straightFlush(stZnakov) * 100);
		System.out.println();

		System.out.println("Four of a kind:");
		System.out.printf("%.2f\n", fourOfAKind(stZnakov) * 100);
		System.out.println();

		System.out.println("Full house:");
		System.out.printf("%.2f\n", fullHouse(stZnakov) * 100);
		System.out.println();

		System.out.println("Flush:");
		System.out.printf("%.2f\n", flush(stZnakov) * 100);
		System.out.println();

		System.out.println("Straight:");
		System.out.printf("%.2f\n", straight(stZnakov) * 100);
		System.out.println();

		System.out.println("Three of a kind:");
		System.out.printf("%.2f\n", threeOfAKind(stZnakov) * 100);
		System.out.println();

		System.out.println("Two pair:");
		System.out.printf("%.2f\n", twoPair(stZnakov) * 100);
		System.out.println();

		System.out.println("Pair:");
		System.out.printf("%.2f\n", pair(stZnakov) * 100);
		System.out.println();

		System.out.println("Nothing:");
		System.out.printf("%.2f\n", nothing(stZnakov) * 100);
		System.out.println();

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

		System.out.println("Visok par z visoko:");
		System.out.printf("%.2f\n", visokParZEnoDodatnoVisoko(stZnakov) * 100);
		System.out.println();

		System.out.println("Srednji par z visoko:");
		System.out.printf("%.2f\n", srednjiParZEnoDodatnoVisoko(stZnakov) * 100);
		System.out.println();

		System.out.println("Nizek par z visoko:");
		System.out.printf("%.2f\n", nizkiParZEnoDodatnoVisoko(stZnakov) * 100);
		System.out.println();

		System.out.println("Visok par brez dodatne visoke:");
		System.out.printf("%.2f\n", visokParBrezDodatneVisoke(stZnakov) * 100);
		System.out.println();

		System.out.println("Srednji par brez dodatne visoke:");
		System.out.printf("%.2f\n", srednjiParBrezDodatneVisoke(stZnakov) * 100);
		System.out.println();

		System.out.println("Nizek par brez dodatne visoke:");
		System.out.printf("%.2f\n", nizkiParBrezDodatneVisoke(stZnakov) * 100);
		System.out.println();

		System.out.println();
		System.out.println();

		System.out.println("Sestevek procentov parov brez in z eno visoko:");
		System.out.printf("%.2f\n", sestevekProcentovParovBrezInZEnoVisoko(stZnakov) * 100);
		System.out.println();

		System.out.println("Procent parov z vec visokimi");
		System.out.printf("%.2f\n", procentovParaZVecVisokimi(stZnakov) * 100);
		System.out.println();

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

		System.out.println("Flush draw brez parov a pustimo straighte");
		System.out.printf("%.2f\n", flushDrawBrezParovAPustimoStraighte(stZnakov) * 100);
		System.out.println();

		System.out.println("Double ended straight draw brez parov in brez flushov:");
		System.out.printf("%.2f\n", doubleEndedStraightDrawBrezParovInBrezFlushov(stZnakov) * 100);
		System.out.println();

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Moznost, da ima nekdo od ostalih igralcev to kombinacijo. \nKombinacije so urejene po padajoci moci.");
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Royal Flush:");
		System.out.printf("%.2f\n", nekdoRoyalFlush(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Straight Flush:");
		System.out.printf("%.2f\n", nekdoStraightFlush(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Four Of A Kind:");
		System.out.printf("%.2f\n", nekdoFourOfAKind(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Full House:");
		System.out.printf("%.2f\n", nekdoFullHouse(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Flush:");
		System.out.printf("%.2f\n", nekdoFlush(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Straight:");
		System.out.printf("%.2f\n", nekdoStraight(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Three Of A Kind:");
		System.out.printf("%.2f\n", nekdoThreeOfAKind(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Two Pair:");
		System.out.printf("%.2f\n", nekdoTwoPair(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Visok Par Z Eno Dodatno Visoko:");
		System.out.printf("%.2f\n", nekdoVisokPairZVisoko(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Visok Par Brez Dodatne Visoke:");
		System.out.printf("%.2f\n", nekdoVisokPairBrezVisoke(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Srednji Par Z Dodatno Visoko:");
		System.out.printf("%.2f\n", nekdoSrednjiParZVisoko(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Srednji Par Brez Dodatne Visoke:");
		System.out.printf("%.2f\n", nekdoSrednjiParBrezVisoke(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Nizek Par Z Dodatno Visoko:");
		System.out.printf("%.2f\n", nekdoNizekParZVisoko(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println("Vsaj nekdo od ostalih ima Nizek Par Brez Dodatne Visoke:");
		System.out.printf("%.2f\n", nekdoNizekParBrezVisoke(stZnakov, stOstalihIgralcev) * 100);
		System.out.println();

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

		System.out.println("Kumulativno:");
		izpisovalnaFunkcija(stZnakov, stOstalihIgralcev);


		




		// System.out.println("Royal flush:");
		// System.out.printf("%.50f\n", royalFlush(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Straight flush:");
		// System.out.printf("%.50f\n", straightFlush(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Four of a kind:");
		// System.out.printf("%.50f\n", fourOfAKind(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Full house:");
		// System.out.printf("%.50f\n", fullHouse(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Flush:");
		// System.out.printf("%.50f\n", flush(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Straight:");
		// System.out.printf("%.50f\n", straight(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Three of a kind:");
		// System.out.printf("%.50f\n", threeOfAKind(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Two pair:");
		// System.out.printf("%.50f\n", twoPair(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Pair:");
		// System.out.printf("%.50f\n", pair(stZnakov) * 100);
		// System.out.println();

		// System.out.println("Nothing:");
		// System.out.printf("%.50f\n", nothing(stZnakov) * 100);
		// System.out.println();

		sc.close();
	}

}