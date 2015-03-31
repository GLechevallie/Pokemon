import java.util.Scanner;

public class Joueur {

	private String nom;
	private boolean Humain;

	public Joueur (String n, boolean hum){
		nom = n;
		Humain = hum;
	}

	public Action Decision (Pokemon attaquant, Pokemon defenseur, String meteo, int compteurMeteo, String terrain, int compteurTerrain, int gravite, int zoneMagique, int distorsion, int tourniquet, boolean affichage){

		boolean mevo = false;

		if (!attaquant.getChargement().equals("-")){
			return new Action (new Attaque(attaquant.getChargement()), false);
		}

		if (attaquant.isChoiced() || attaquant.getEnColere() > 0 || attaquant.isBrouhaha() || attaquant.getEncore() > 0 || attaquant.isDecharge()){
			try{
				if(attaquant.getDerniereAttaque().getPP() > 0 && !attaquant.getDerniereAttaque().isPossessif() && !attaquant.isTourmente() && !((attaquant.getTaunted() > 0 || attaquant.isAssaut()) && !(attaquant.getDerniereAttaque().getPuissance() > 0))){
					return new Action (attaquant.getDerniereAttaque(), false);
				}
				else{
					return new Action (new Attaque ("Struggle"), false);
				}
			}
			catch(Exception e){}
		}

		if (Humain){
			Scanner sc = new Scanner (System.in);
			System.out.print("\n" + defenseur.getNom());

			if (defenseur.getSexe().equals("femelle")){		
				System.out.print(" (f)");
			}
			if (defenseur.getSexe().equals("male")){		
				System.out.print(" (m)");
			}
			System.out.print(" :	" + ((float) (int) (defenseur.getStatsModifiees()[0] / defenseur.getStatsInitiales()[0] * 1000))/10 + "% (" + defenseur.getStatut());

			if (defenseur.isSubstitute()){
				System.out.print(", clone");
			}
			System.out.println(")");

			System.out.print("\n" + attaquant.getNom());

			if (attaquant.getSexe().equals("femelle")){		
				System.out.print(" (f)");
			}
			if (attaquant.getSexe().equals("male")){		
				System.out.print(" (m)");
			}
			System.out.print(" :	" + (int) attaquant.getStatsModifiees()[0] + " / " + attaquant.getStatsInitiales()[0] + " (" + attaquant.getStatut()+ ", " + attaquant.getObjet());
			if (attaquant.isSubstitute()){
				System.out.print(", clone");
			}
			System.out.println(")");

			boolean PPwasted = true;
			for (int i=0; i<4; ++i){
				if (!attaquant.getMoves()[i].getNom().equals(attaquant.getEntrave())){
					System.out.println(i+1 + " - " + attaquant.getMoves()[i].getNom() + " (" + attaquant.getMoves()[i].getPP() + " PP)");
					if (attaquant.getMoves()[i].getPP() > 0){
						if (!(attaquant.isTourmente() && attaquant.getMoves()[i].getNom().equals(attaquant.getDerniereAttaque().getNom()))){
							if (!((attaquant.getTaunted() > 0 || attaquant.isAssaut()) && !(attaquant.getMoves()[i].getCategorie().equals("0") || attaquant.getMoves()[i].getCategorie().equals("4") || attaquant.getMoves()[i].getCategorie().equals("6") || attaquant.getMoves()[i].getCategorie().equals("7") || attaquant.getMoves()[i].getCategorie().equals("8")))){
								if (!attaquant.getMoves()[i].isPossessif()){
									PPwasted = false;
								}
							}
							else{
								//								System.out.println("Attaque bloquee par la Provoc");
							}
						}
						else{
							//							System.out.println("Attaque bloquee par la Tourmente.");
						}
					}
					else{
						//						System.out.println("Plus de PP");
					}
				}
				if (attaquant.getMoves()[i].getNom().equals(attaquant.getEntrave())){
					//					System.out.println("(capacite sous entrave)");
				}
			}

			if (attaquant.canMevo()){
				System.out.println("M - Mega-evolution");
			}
			System.out.println("S - Switcher");

			System.out.println("Que doit faire " + attaquant.getNom() + " ?");
			String rep = sc.nextLine();
			if (PPwasted){
				System.out.println("Plus d'attaques!");
				return new Action (new Attaque("Struggle"), false);
			}
			if (! (rep.equals("1") || rep.equals("2") || rep.equals("3") || rep.equals("4")  || rep.equals("M1")  || rep.equals("M2")  || rep.equals("M3")  || rep.equals("M4") || rep.equals("S")  )){
				System.out.println("Reponse invalide. Recommencez.\n");
				return this.Decision(attaquant, defenseur, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);
			}

			if (rep.startsWith("M")){
				mevo = true;
				rep = "" + rep.charAt(1);
			}
			if (rep.equals("S")){
				return new Action(true);
			}

			if (attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getPP() <= 0){
				System.out.println("Il n'y a plus de PP pour cette capacite!");
				return this.Decision(attaquant, defenseur, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);
			}
			if (attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getNom().equals(attaquant.getEntrave())){
				System.out.println("Cette capacite est sous entrave !");
				return this.Decision(attaquant, defenseur, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);
			}

			if ((attaquant.getTaunted() > 0 || attaquant.isAssaut()) && !attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getCategorie().equals("0") && !attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getCategorie().equals("4") && !attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getCategorie().equals("6") && !attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getCategorie().equals("7") && !attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getCategorie().equals("8")){
				System.out.println("A cause de la provocation, vous ne pouvez pas utiliser cette attaque.");
				return this.Decision(attaquant, defenseur, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);
			}
			if (attaquant.isTourmente() && attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].getNom().equals(attaquant.getDerniereAttaque().getNom())){
				System.out.println(attaquant.getNom() + " est tourmente et ne peut pas utiliser cette attaque !");
				return this.Decision(attaquant, defenseur, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);
			}
			if (attaquant.getMoves()[Integer.valueOf(rep).intValue() -1].isPossessif()){
				System.out.println(attaquant.getNom() + " ne peut pas utilise cette attaque a cause de Possessif !");
				return this.Decision(attaquant, defenseur, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);
			}

			return new Action (attaquant.getMoves()[Integer.valueOf(rep).intValue() -1], mevo);
		}

		// CODE IA
		else{

			long chrono = java.lang.System.currentTimeMillis() ;

			boolean PPwasted = true;
			for (int i=0; i<4; ++i){
				if (!attaquant.getMoves()[i].getNom().equals(attaquant.getEntrave())){
					if (!(attaquant.getTaunted() > 0 || attaquant.isAssaut()) || ((attaquant.getTaunted() > 0 || attaquant.isAssaut()) && (attaquant.getMoves()[i].getCategorie().equals("0") || attaquant.getMoves()[i].getCategorie().equals("4") || attaquant.getMoves()[i].getCategorie().equals("6") || attaquant.getMoves()[i].getCategorie().equals("7") || attaquant.getMoves()[i].getCategorie().equals("8") || attaquant.getMoves()[i].getNom().equals("Future Sight")))){
						if (attaquant.getMoves()[i].getPP() > 0){
							if (!(attaquant.isTourmente() && attaquant.getMoves()[i].getNom().equals(attaquant.getDerniereAttaque().getNom()))){
								//								System.out.println("Peut utiliser " + attaquant.getMoves()[i].getNom());
								if (!attaquant.getMoves()[i].isPossessif()){
									PPwasted = false;
								}
							}
							else{
								//								System.out.println("Bloquee par Tourmente");
							}
						}
						else{
							//							System.out.println("Plus de PP");
						}
					}
					else{
						//						System.out.println("Attaque bloquee par Provoc");
					}
				}
				else{
					//					System.out.println("Attaque sous entrave");
				}
			}
			if (PPwasted){
				return new Action (new Attaque("Struggle"), false);
			}

			Joueur Bot = new Joueur ("Cyber-Guillaume", false);

			Pokemon doubleAtq = attaquant.clone();
			Pokemon doubleDef = defenseur.clone();

			for (int i=0; i<4; ++i){
				doubleAtq.getMoves()[i].setPrecision(101);
				doubleAtq.getMoves()[i].setCritique(-100);
				if (doubleAtq.getMoves()[i].getTrouille() < 100){
					doubleAtq.getMoves()[i].setTrouille(-100);
				}
				if (doubleAtq.getMoves()[i].getTaux() < 100){
					doubleAtq.getMoves()[i].setTaux(-100);
				}

				doubleDef.getMoves()[i].setPrecision(101);
				doubleDef.getMoves()[i].setCritique(-100);
				if (doubleDef.getMoves()[i].getTrouille() < 100){
					doubleDef.getMoves()[i].setTrouille(-100);
				}
				if (doubleDef.getMoves()[i].getTaux() < 100){
					doubleDef.getMoves()[i].setTaux(-100);
				}
			}

			Combat simulation;
			Action actionAtq;
			Action actionDef;

			boolean[] tabMevo = {true, false};

			Action reponseIA = new Action (new Attaque("Struggle"), false);

			int avantage = -2147483648;
			int maxAvantage = 2147483647;
			boolean KOsur = false;
			float PVminDef = doubleDef.getStatsModifiees()[0];
			float PVminDefTouche = PVminDef;
			float PVmaxAtq = 0;

			boolean attaqueDuDouble = true;

			for (int iAtq=0; iAtq<4; ++iAtq){
				doubleAtq = attaquant.clone();
				doubleDef = defenseur.clone();
				if (!attaquant.getMoves()[iAtq].getNom().equals(attaquant.getEntrave())){
					//System.out.println("Entrave : non");
					if (doubleAtq.getMoves()[iAtq].getPP() > 0 && !((doubleAtq.getTaunted() > 0 || doubleAtq.isAssaut()) && !doubleAtq.getMoves()[iAtq].getCategorie().equals("0") && !doubleAtq.getMoves()[iAtq].getCategorie().equals("4") && !doubleAtq.getMoves()[iAtq].getCategorie().equals("6") && !doubleAtq.getMoves()[iAtq].getCategorie().equals("7") && !doubleAtq.getMoves()[iAtq].getCategorie().equals("8")) && !(doubleAtq.getCompteurAbrite() > 1 && (doubleAtq.getMoves()[iAtq].getNom().equals("Detect") || doubleAtq.getMoves()[iAtq].getNom().equals("Endure") || doubleAtq.getMoves()[iAtq].getNom().equals("Protect") || doubleAtq.getMoves()[iAtq].getNom().equals("King's Shield")))){
						//System.out.println("PP : OK , Provoc : OK , Abri : OK");
						if (!(attaquant.isTourmente() && attaquant.getMoves()[iAtq].getNom().equals(attaquant.getDerniereAttaque().getNom()))){
							//							System.out.println("Tourmente : OK");
							//							System.out.println(doubleAtq.getNom() + " peut utiliser " + doubleAtq.getMoves()[iAtq].getNom());
							if (!attaquant.getMoves()[iAtq].isPossessif()){

								//								for (int i=0; i<4; ++i){
								//									doubleAtq.getMoves()[i].setPrecision(101);
								//									doubleAtq.getMoves()[i].setCritique(-100);
								//									doubleAtq.getMoves()[i].setTrouille(-100);
								//									doubleAtq.getMoves()[i].setTaux(-100);
								//
								//									doubleDef.getMoves()[i].setPrecision(101);
								//									doubleDef.getMoves()[i].setCritique(-100);
								//									doubleDef.getMoves()[i].setTrouille(-100);
								//									doubleDef.getMoves()[i].setTaux(-100);
								//								}

								for (int mAtq=0; mAtq<2; ++mAtq){

									if (!doubleAtq.canMevo()){
										mAtq = 1;
									}

									int avAtq = 1000;
									int maxAvAtq = -1000;
									boolean KO = true;
									float PVdef = 0;
									float PVdefTouche = doubleDef.getStatsInitiales()[0];
									float PVatq = doubleAtq.getStatsInitiales()[0];
									actionAtq = new Action (doubleAtq.getMoves()[iAtq], tabMevo[mAtq]);

									if (reponseIA.getAttaque().getNom().equals("Struggle")){
										reponseIA = actionAtq;
									}

									for (int iDef=0; iDef<4; ++iDef){
										for (int mDef=0; mDef<2; ++mDef){
											doubleAtq = attaquant.clone();
											doubleDef = defenseur.clone();
											if (doubleDef.getMoves()[iDef].getPP() > 0 && !doubleDef.getMoves()[iDef].isPossessif() && (!(doubleDef.isTourmente() && doubleDef.getMoves()[iAtq].getNom().equals(doubleDef.getDerniereAttaque().getNom()))) && !((doubleDef.getTaunted() > 0 || doubleDef.isAssaut()) && !doubleDef.getMoves()[iDef].getCategorie().equals("0") && !doubleDef.getMoves()[iDef].getCategorie().equals("4") && !doubleDef.getMoves()[iDef].getCategorie().equals("6") && !doubleDef.getMoves()[iDef].getCategorie().equals("7") && !doubleDef.getMoves()[iDef].getCategorie().equals("8")) && !(doubleDef.getCompteurAbrite() > 1 && (doubleDef.getMoves()[iDef].getNom().equals("Detect") || doubleDef.getMoves()[iDef].getNom().equals("Endure") || doubleDef.getMoves()[iDef].getNom().equals("Protect") || doubleDef.getMoves()[iDef].getNom().equals("King's Shield")))){

												for (int i=0; i<4; ++i){
													doubleAtq.getMoves()[i].setPrecision(101);
													doubleAtq.getMoves()[i].setCritique(-100);
													if (doubleAtq.getMoves()[i].getTrouille() < 100){
														doubleAtq.getMoves()[i].setTrouille(-100);
													}
													if (doubleAtq.getMoves()[i].getTaux() < 100){
														doubleAtq.getMoves()[i].setTaux(-100);
													}

													doubleDef.getMoves()[i].setPrecision(101);
													doubleDef.getMoves()[i].setCritique(-100);
													if (doubleDef.getMoves()[i].getTrouille() < 100){
														doubleDef.getMoves()[i].setTrouille(-100);
													}
													if (doubleDef.getMoves()[i].getTaux() < 100){
														doubleDef.getMoves()[i].setTaux(-100);
													}
												}

												if (!doubleDef.canMevo()){
													mDef = 1;
												}

												actionDef = new Action (doubleDef.getMoves()[iDef], tabMevo[mDef]);

												simulation = new Combat (Bot, Bot, doubleAtq, doubleDef, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, false);
												simulation.simuleTour(actionAtq, actionDef);
												int avAD = simulation.Avantage();
												//System.out.println("Avantage de " + doubleAtq.getMoves()[iAtq].getNom() + " si " +  doubleDef.getMoves()[iDef].getNom() + " : " + avAD);
												if (avAD < avAtq){
													avAtq = avAD;
												}
												if (avAD > maxAvAtq){
													maxAvAtq = avAD;
												}
												if (!doubleDef.getStatut().equals("KO")){
													KO = false;
												}
												if (PVdef < doubleDef.getStatsModifiees()[0]){
													PVdef = doubleDef.getStatsModifiees()[0];
												}
												if (PVdefTouche > doubleDef.getStatsModifiees()[0]){
													PVdefTouche = doubleDef.getStatsModifiees()[0];
												}
												if (PVatq > doubleAtq.getStatsModifiees()[0]){
													PVatq = doubleAtq.getStatsModifiees()[0];
												}
											}
										}
									}
									if (avAtq < -100){
										avAtq = -100;
									}
									if (maxAvAtq > 100){
										maxAvAtq = 100;
									}
									//System.out.println("\nAvantage de " + actionAtq.toString() + " : " + avAtq + " / PV restants a l'adversaire : " + PVdef);
									//System.out.println("Degats max de " + actionAtq.toString() + " : " + PVdefTouche);

									if (attaquant.getTalent().equals("Gale Wings") && actionAtq.getAttaque().getType().equals("Flying")){
										actionAtq.getAttaque().setPriorite(actionAtq.getAttaque().getPriorite()+1);
									}
									actionAtq = new Action (attaquant.getMoves()[iAtq], tabMevo[mAtq]);

									boolean changement = false;
									if (KO){
										if (!KOsur){
											changement = true;
											//System.out.println("Changement pour assurer le KO.");
										}
										else{
											// Priorite
											if (actionAtq.getAttaque().getPriorite() > reponseIA.getAttaque().getPriorite()){
												changement = true;
												//System.out.println("Changement pour une plus grande priorite.");
											}
											if (actionAtq.getAttaque().getPriorite() == reponseIA.getAttaque().getPriorite()){
												// Precision
												if ((actionAtq.getAttaque().getPrecision() > reponseIA.getAttaque().getPrecision() && reponseIA.getAttaque().getPrecision() < 100) && (actionAtq.getAttaque().getCategorie().equals("0") || actionAtq.getAttaque().getCategorie().equals("4") || actionAtq.getAttaque().getCategorie().equals("6") || actionAtq.getAttaque().getCategorie().equals("7") || actionAtq.getAttaque().getCategorie().equals("8"))){
													changement = true;
													//System.out.println("Changement pour plus de precision.");
												}
												if (actionAtq.getAttaque().getPrecision() == reponseIA.getAttaque().getPrecision()  || actionAtq.getAttaque().getPrecision() >= 100 && !changement){
													// Puissance
													if (PVdef < PVminDef){
														changement = true;
														//System.out.println("Changement pour plus de degats.");
													}
													if (PVdef == PVminDef){
														// Contact
														if (reponseIA.getAttaque().isContact() && !actionAtq.getAttaque().isContact()){
															changement  = true;
															//System.out.println("Changement pour eviter le contact.");
														}
														if ((reponseIA.getAttaque().isContact() && actionAtq.getAttaque().isContact()) || (!reponseIA.getAttaque().isContact() && !actionAtq.getAttaque().isContact())){
															// Degats potentiels
															if (PVdefTouche < PVminDefTouche){
																changement = true;
																//System.out.println("Changement pour plus de degats potentiels.");
															}
															if (PVatq == PVmaxAtq){
																// Survie
																if (PVatq > PVmaxAtq){
																	changement = true;
																	System.out.println("Changement pour plus de survie.");
																}
																else{
																	// Recul
																	if (actionAtq.getAttaque().getRecul() > reponseIA.getAttaque().getRecul()){
																		changement = true;
																		//System.out.println("Changement pour moins de recul.");
																	}
																	if (actionAtq.getAttaque().getRecul() == reponseIA.getAttaque().getRecul()){
																		// Taux de critique
																		if (actionAtq.getAttaque().getCritique() > reponseIA.getAttaque().getCritique()){
																			changement = true;
																			//System.out.println("Changement pour plus de chances de critique.");
																		}
																		if (actionAtq.getAttaque().getCritique() == reponseIA.getAttaque().getCritique()){
																			// Effet secondaire
																			if (actionAtq.getAttaque().getTaux() > reponseIA.getAttaque().getTaux()){
																				changement = true;
																				//System.out.println("Changement pour plus de chances d'effet secondaire.");
																			}
																			if (actionAtq.getAttaque().getTaux() == reponseIA.getAttaque().getTaux()){
																				// Taux de trouille
																				if (actionAtq.getAttaque().getTrouille() > reponseIA.getAttaque().getTrouille()){
																					changement = true;
																					//System.out.println("Changement pour plus de chances d'apeurer.");
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
										KOsur = true;
									}
									if (!KOsur && avAtq > avantage){
										changement = true;
										//System.out.print("Changement pour un meilleur avantage. (" + avAtq + ")\n");
									}
									if (!KOsur && avAtq == avantage){
										// Precision
										if ((actionAtq.getAttaque().getPrecision() > reponseIA.getAttaque().getPrecision() && reponseIA.getAttaque().getPrecision() < 100) && (actionAtq.getAttaque().getCategorie().equals("0") || actionAtq.getAttaque().getCategorie().equals("4") || actionAtq.getAttaque().getCategorie().equals("6") || actionAtq.getAttaque().getCategorie().equals("7") || actionAtq.getAttaque().getCategorie().equals("8"))){
											changement = true;
											//System.out.println("Changement pour plus de precision.");
										}
										if (actionAtq.getAttaque().getPrecision() == reponseIA.getAttaque().getPrecision()  || actionAtq.getAttaque().getPrecision() >= 100 && !changement){
											// Puissance
											if (PVdef < PVminDef){
												changement = true;
												//System.out.println("Changement pour plus de degats.");
											}
											if (PVdef == PVminDef){
												// Contact
												if (reponseIA.getAttaque().isContact() && !actionAtq.getAttaque().isContact()){
													changement  = true;
													//System.out.println("Changement pour eviter le contact.");
												}
												if ((reponseIA.getAttaque().isContact() && actionAtq.getAttaque().isContact()) || (!reponseIA.getAttaque().isContact() && !actionAtq.getAttaque().isContact())){
													if (maxAvAtq > maxAvantage){
														changement = true;
														//System.out.println("Changement pour un meilleur avantage potentiels. (" + maxAvantage + " --> " + maxAvAtq + ")");
													}
													else{
														// Degats potentiels
														if (PVdefTouche < PVminDefTouche){
															changement = true;
															//System.out.println("Changement pour plus de degats potentiels. (" + PVminDefTouche + " --> " + PVdefTouche + ")");
														}
														if (PVdefTouche == PVminDefTouche){
															// Survie
															if (PVatq > 1.2*PVmaxAtq){
																changement = true;
																//System.out.println("Changement pour plus de survie.");
															}
															else{
																// Recul
																if (actionAtq.getAttaque().getRecul() > reponseIA.getAttaque().getRecul()){
																	changement = true;
																	//System.out.println("Changement pour moins de recul.");
																}
																if (actionAtq.getAttaque().getRecul() == reponseIA.getAttaque().getRecul()){
																	// Priorite
																	if (actionAtq.getAttaque().getPriorite() > reponseIA.getAttaque().getPriorite()){
																		changement = true;
																		//System.out.println("Changement pour une plus grande priorite.");
																	}
																	if (actionAtq.getAttaque().getPriorite() == reponseIA.getAttaque().getPriorite()){
																		// Taux de critique
																		if (actionAtq.getAttaque().getCritique() > reponseIA.getAttaque().getCritique()){
																			changement = true;
																			//System.out.println("Changement pour plus de chances de critique.");
																		}
																		if (actionAtq.getAttaque().getCritique() == reponseIA.getAttaque().getCritique()){
																			// Effet secondaire
																			if (actionAtq.getAttaque().getTaux() > reponseIA.getAttaque().getTaux()){
																				changement = true;
																				//System.out.println("Changement pour plus de chances d'effet secondaire.");
																			}
																			if (actionAtq.getAttaque().getTaux() == reponseIA.getAttaque().getTaux()){
																				// Taux de trouille
																				if (actionAtq.getAttaque().getTrouille() > reponseIA.getAttaque().getTrouille()){
																					changement = true;
																					//System.out.println("Changement pour plus de chances d'apeurer.");
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}

									if (changement){
										attaqueDuDouble = false;
										avantage = avAtq;
										maxAvantage = maxAvAtq;
										reponseIA = new Action (attaquant.getMoves()[iAtq], tabMevo[mAtq]);
										PVminDef = PVdef;
										PVmaxAtq = PVatq;
										PVminDefTouche = PVdefTouche;
									}
								}
							}
						}
					}
				}
			}

			if (attaqueDuDouble){
				for (int i=0; i<4; ++i){
					if (attaquant.getMoves()[i].getPP() > 0){
						return new Action (attaquant.getMoves()[i], true);
					}
				}
			}

			long chrono2 = java.lang.System.currentTimeMillis() ;
			long temps = chrono2 - chrono ;
			if (affichage){
				System.out.println("\nDecision de l'IA prise en " + (((double)(temps))/1000) + " s") ; 
			}

			//System.out.println("Decision de " + attaquant.getNom() + " : " + reponseIA.toString() + "\n");
			return reponseIA;
		}
	}	

	public String getNom() {
		return nom;
	}

	public boolean isHumain() {
		return Humain;
	}

}
