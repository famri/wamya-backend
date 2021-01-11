/*******************************************
* INSERTING INTERNATIONAL CALLING CODES
*******************************************/ 
INSERT INTO international_calling_code(id, value, country_name, flag_path, enabled) VALUES(1, '+216', 'TUNISIA', 'assets/images/icons/tunisia.png', TRUE) ON CONFLICT DO NOTHING;
INSERT INTO international_calling_code(id, value, country_name, flag_path, enabled) VALUES(2, '+33', 'FRANCE', 'assets/images/icons/france.png', FALSE) ON CONFLICT DO NOTHING;

/*******************************************
* INSERTING ENGINE TYPES
*******************************************/ 
INSERT INTO ENGINE_TYPE(id,code) VALUES(1,'UTILITY') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(2,'PICKUP') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(3,'BUS')  ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(4,'MINIBUS')  ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(5,'VAN_L1H1') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(6,'VAN_L2H2') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(7,'VAN_L3H2') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(8,'FLATBED_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(9,'BOX_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(10,'REFRIGERATED_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(11,'TANKER') ON CONFLICT DO NOTHING; 
INSERT INTO ENGINE_TYPE(id,code) VALUES(12,'DUMP_TRUCK') ON CONFLICT DO NOTHING; 
INSERT INTO ENGINE_TYPE(id,code) VALUES(13,'HOOK_LIFT_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(14,'TANK_TRANSPORTER') ON CONFLICT DO NOTHING;

/*******************************************
* INSERTING ENGINE TYPES TRANSLATIONS
*******************************************/ 
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(1,'en_US', 'Utility vehicule', 'Examples of utility vehicule: Renault Kangoo, Ciroën Jumper, Peugeot Partner') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(1,'fr_FR', 'Véhicule utilitaire', 'Examples de véhicule utilitaire: Renault Kangoo, Ciroën Jumper, Peugeot Partner') ON CONFLICT DO NOTHING;


INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(2,'en_US', 'Pickup vehicule', 'Examples of pickup vehicule: Isuzu D-max, Mazda BT-50, Mahindra SC') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(2,'fr_FR', 'Véhicule pickup', 'Examples de véhicule pickup: Isuzu D-max, Mazda BT-50, Mahindra SC') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(3,'en_US', 'Bus', 'Examples of bus: Mercedes Tourismo, MAN R07, Irisbus Recreo') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(3,'fr_FR', 'Bus', 'Examples de bus: Mercedes Tourismo, MAN R07, Irisbus Recreo') ON CONFLICT DO NOTHING;


INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(4,'en_US', 'Minibus', 'Examples of minibus: Hyundai County, Toyota Coaster, Otokar Minibus') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(4,'fr_FR', 'Minibus', 'Examples de minibus: Hyundai County, Toyota Coaster, Otokar Minibus') ON CONFLICT DO NOTHING;


INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(5,'en_US', 'L1H1 vehicule', 'Examples of L1H1 vehicule: Renault Trafic, Peugeot Boxer, Volkswagen Transporter') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(5,'fr_FR', 'Véhicule L1H1', 'Examples de Véhicule L1H1: Renault Trafic, Peugeot Boxer, Volkswagen Transporter') ON CONFLICT DO NOTHING;


INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(6,'en_US', 'L2H2 vehicule', 'Examples of L2H2 vehicule: Renault Master L2H2, Peugeot Boxer L2H2, Citroën Jumper L2H2') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(6,'fr_FR', 'Véhicule L2H2', 'Examples de Véhicule L2H2: Renault Master L2H2, Peugeot Boxer L2H2, Citroën Jumper L2H2') ON CONFLICT DO NOTHING;


INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(7,'en_US', 'L3H3 vehicule', 'Examples of L3H3 vehicule: Renault Master L3H3, Peugeot Boxer L3H3, Fiat Ducato L3H3') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(7,'fr_FR', 'Véhicule L3H3', 'Examples de Véhicule L1H1: Renault Master L3H3, Peugeot Boxer L3H3, Fiat Ducato L3H3') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(8,'en_US', 'L4H3 vehicule', 'Examples of L4H3 vehicule: Renault Master L4H3, Peugeot Boxer L4H3, Fiat Ducato L3H3') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(8,'fr_FR', 'Véhicule L4H3', 'Examples de Véhicule L4H3: Renault Master L4H3, Peugeot Boxer L4H3, Fiat Ducato L3H3') ON CONFLICT DO NOTHING;


INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(9,'en_US', 'Flatbed truck', 'Examples of flatbed truck: Man TGA, Renault Premium, Iveco Trakker') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(9,'fr_FR', 'Camion plateau', 'Examples de camion plateau: Man TGA, Renault Premium, Iveco Trakker') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(10,'en_US', 'Box truck', 'Examples of box truck: Renault Premium, Iveco Eurocargo, MAN TGX') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(10,'fr_FR', 'Fourgon caisse', 'Examples de fourgon caisse: Renault Premium, Iveco Eurocargo, MAN TGX') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(11,'en_US', 'Refrigerated truck', 'Examples of refrigerated truck: Renault Midlum, Scania P, Mercedes Atego') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(11,'fr_FR', 'Camion frigorifique', 'Examples de camion frigorifique: Renault Midlum, Scania P, Mercedes Atego') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(12,'en_US', 'Tanker', 'Examples of tanker: MAN TGS, Renault Premium, Scania G') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(12,'fr_FR', 'Camion-citerne', 'Examples de camion-citerne: MAN TGS, Renault Premium, Scania G') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(13,'en_US', 'Dump truck', 'Examples of dump truck: Iveco Stralis, Renault Kerax, Mercedes Arocs') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(13,'fr_FR', 'Camion à benne basculante', 'Examples de camion à benne basculante: Iveco Stralis, Renault Kerax, Mercedes Arocs') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(14,'en_US', 'Hook lift truck', 'Examples of hook lift truck: Iveco Stralis, Mercedes Atego, Man TGS') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(14,'fr_FR', 'Camion Ampliroll', 'Examples de Camion Ampliroll: Iveco Stralis, Mercedes Atego, Man TGS') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(15,'en_US', 'Tank transporter', 'Examples of tank transporter: Scania D, Mercedes Actros, Renault Premium') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(15,'fr_FR', 'Camion porte char', 'Examples de camion porte char: Scania D, Mercedes Actros, Renault Premium') ON CONFLICT DO NOTHING;

/*************************************
* INSERTING JOURNEY REQUEST STATUSES
*************************************/
INSERT INTO public.journey_request_status( id, code, description) VALUES (1, 'OPENED', 'Journey request was saved and is waiting for proposals.')ON CONFLICT DO NOTHING;
INSERT INTO public.journey_request_status( id, code, description) VALUES (2, 'CANCELED', 'Journey request was canceled by the client.')ON CONFLICT DO NOTHING;
INSERT INTO public.journey_request_status( id, code, description) VALUES (3, 'EXPIRED', 'Client did not accept any proposal for this journey request before its end date.')ON CONFLICT DO NOTHING;
INSERT INTO public.journey_request_status( id, code, description) VALUES (4, 'FULFILLED', 'Client accepted a proposal for this journey request before its end date.')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (1, 'en_US', 'opened')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (1, 'fr_FR', 'ouvert')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (2, 'en_US', 'canceled')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (2, 'fr_FR', 'annulé')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (3, 'en_US', 'expired')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (3, 'fr_FR', 'expiré')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (4, 'en_US', 'fulfilled')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (4, 'fr_FR', 'rempli')ON CONFLICT DO NOTHING;

			
/*************************************
* INSERTING JOURNEY PROPOSAL STATUSES
*************************************/
INSERT INTO public.journey_proposal_status(id, code, description) VALUES (1, 'SUBMITTED', 'Journey proposal was submitted to client.')ON CONFLICT DO NOTHING;
INSERT INTO public.journey_proposal_status(id, code, description) VALUES (2, 'CANCELED', 'Client canceled the journey request.')ON CONFLICT DO NOTHING;
INSERT INTO public.journey_proposal_status(id, code, description) VALUES (3, 'REJECTED', 'Client rejected this journey proposal.')ON CONFLICT DO NOTHING;
INSERT INTO public.journey_proposal_status(id, code, description) VALUES (4, 'ACCEPTED', 'Client accepted this journey proposal.')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (1, 'en_US', 'submitted')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (1, 'fr_FR', 'envoyée')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (2, 'en_US', 'canceled')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (2, 'fr_FR', 'annulée')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (3, 'en_US', 'rejected')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (3, 'fr_FR', 'rejetée')ON CONFLICT DO NOTHING;

INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (4, 'en_US', 'accepted')ON CONFLICT DO NOTHING;
INSERT INTO public.localized_journey_proposal_status(id, locale, value) VALUES (4, 'fr_FR', 'acceptée')ON CONFLICT DO NOTHING;










