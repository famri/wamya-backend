/*******************************************
* INSERTING INTERNATIONAL CALLING CODES
*******************************************/
INSERT INTO international_calling_code(id, value, country_name, flag_path, enabled) VALUES(2, '+33', 'FRANCE', 'assets/images/icons/france.png', FALSE) ON CONFLICT DO NOTHING;

/*******************************************
* INSERTING ENGINE TYPES
*******************************************/ 
INSERT INTO ENGINE_TYPE(id,code) VALUES(1,'UTILITY') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(2,'PICKUP') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(3,'VAN_L1H1') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(4,'VAN_L1H2') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(5,'VAN_L2H1') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(6,'VAN_L2H2') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(7,'VAN_L3H2') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(8,'VAN_L3H3') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(9,'LIFT_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(10,'BOX_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(11,'FLATBED_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(12,'REFRIGERATED_TRUCK') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(13,'TANKER') ON CONFLICT DO NOTHING;
INSERT INTO ENGINE_TYPE(id,code) VALUES(14,'DUMP_TRUCK') ON CONFLICT DO NOTHING;


/*******************************************
* INSERTING ENGINE TYPES TRANSLATIONS
*******************************************/ 
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(1,'en_US', 'Little utility', 'Dimensions of loading zone: length 1.80m / width: 1.20m / height 1.10m') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(1,'fr_FR', 'Petit utilitaire', 'Dimensions de la zone de chargement: longeur 1.80m / largeur 1.20m / hauteur 1.10m') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(2,'en_US', 'Pickup vehicle', 'A pickup truck is a type of  light-duty truck with an open cargo area for carrying goods and a cab for passengers.') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(2,'fr_FR', 'Camionnette', 'Une camionnette est un type de camion léger avec une zone de chargement ouverte pour transporter des marchandises et une cabine pour des passagers.') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(3,'en_US', 'L1H1 vehicle', 'Dimensions of loading zone: length ~2.50m / height ~1.70m') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(3,'fr_FR', 'Véhicule L1H1', 'Dimensions de la zone de chargement: longeur ~2.50m / hauteur ~1.70m') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(4,'en_US', 'L1H2 vehicle', 'Dimensions of loading zone: length ~2.50m / height ~1.80m') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(4,'fr_FR', 'Véhicule L1H2', 'Dimensions de la zone de chargement: longeur ~2.50m / hauteur ~1.80m') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(5,'en_US', 'L2H1 vehicle', 'Dimensions of loading zone: length ~3.00m / height ~1.70m') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(5,'fr_FR', 'Véhicule L2H1', 'Dimensions de la zone de chargement: longeur ~3.00m / hauteur ~1.70m') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(6,'en_US', 'L2H2 vehicle', 'Dimensions of loading zone: length ~3.00m / height ~1.80m') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(6,'fr_FR', 'Véhicule L2H2', 'Dimensions de la zone de chargement: longeur ~3.00m / hauteur ~1.80m') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(7,'en_US', 'L3H2 vehicle', 'Dimensions of loading zone: length ~3.70m / height ~2.20m') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(7,'fr_FR', 'Véhicule L3H2', 'Dimensions de la zone de chargement: longeur ~3.70m / hauteur ~2.20m') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(8,'en_US', 'L3H3 vehicle', 'Dimensions of loading zone: length ~3.70m / height ~2.60m') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(8,'fr_FR', 'Véhicule L3H3', 'Dimensions de la zone de chargement: longeur ~3.70m /hauteur ~2.60m') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(9,'en_US', 'Lift truck', 'The lift truck is equipped with a lifting mechanism, such as a set of forks or a platform, that can be raised or lowered to pick up and transport loads.') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(9,'fr_FR', 'Monte-charge', 'Le monte-charge est équipé d''un mécanisme de levage, tel qu''un ensemble de fourches ou une plate-forme, qui peut être levé ou abaissé pour ramasser et transporter des charges.') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(10,'en_US', 'Box truck', 'A box truck is a type of truck with an enclosed cargo area used for deliveries, transport of goods, and moving household items.') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(10,'fr_FR', 'Fourgon caisse', 'Un camion fourgon est un type de camion avec une zone de chargement fermée utilisée pour les livraisons, le transport de marchandises et le déplacement d''articles ménagers.') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(11,'en_US', 'Flatbed truck', 'A flatbed truck is a type of heavy truck used for transporting goods on a flat platform located at the rear of the vehicle.') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(11,'fr_FR', 'Camion plateau', 'Un camion plateau est un type de camion lourd utilisé pour le transport de marchandises sur une plateforme plate située à l''arrière du véhicule.') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(12,'en_US', 'Refrigerated truck', 'A refrigerated truck is a type of truck equipped with a refrigeration system that helps maintain temperatures inside the vehicle at specific levels.') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(12,'fr_FR', 'Camion frigorifique', 'Un camion frigorifique est un type de camion équipé d''un système de réfrigération qui permet de maintenir les températures à l''intérieur du véhicule à des niveaux précis.') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(13,'en_US', 'Tanker', 'A tank truck is a type of truck equipped with a cylindrical tank used to transport liquids such as chemicals, fuel or hazardous materials.') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(13,'fr_FR', 'Camion-citerne', 'Un camion-citerne est un type de camion équipé d''un réservoir cylindrique utilisé pour transporter des liquides tels que les produits chimiques, du carburant ou des matières dangereuses.') ON CONFLICT DO NOTHING;

INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(14,'en_US', 'Dump truck', 'A dump truck is a type of truck equipped with a large, hinged, open-box bed at the rear, which can be raised to dump its contents.') ON CONFLICT DO NOTHING;
INSERT INTO LOCALIZED_ENGINE_TYPE(id, locale, name, description) VALUES(14,'fr_FR', 'Camion à benne basculante', 'Un camion à benne basculante est un type de camion équipé d''une grande caisse ouverte à charnière à l''arrière, qui peut être relevée pour vider son contenu.') ON CONFLICT DO NOTHING;


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
INSERT INTO public.localized_journey_request_status(id, locale, value) VALUES (2, 'fr_FR', 'annul?')ON CONFLICT DO NOTHING;

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


			
/*************************************
* INSERTING VEHICULES CONSTRUCTORS
*************************************/

INSERT INTO public.vehicule_constructor(id, name) VALUES (1, 'CITROEN');
INSERT INTO public.vehicule_constructor(id, name) VALUES (2, 'FIAT');
INSERT INTO public.vehicule_constructor(id, name) VALUES (3, 'IVECO');


			
/*************************************
* INSERTING VEHICULES MODELS
*************************************/
INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (1, 'BERLINGO B9 COURT', 1, 4.34, 1.85, 1.8);
INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (2, 'BERLINGO B9 LONG', 1, 4.59, 1.85, 1.8);
INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (3, 'BERLINGO COURT', 1, 4.11, 1.8, 1.72);

INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (4, 'DOBLO CARGO', 2, 4.16, 1.8, 1.71);
INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (5, 'DOBLO CARGO 1.3 MULTIJET', 2, 4.38, 1.81, 1.81);
INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (6, 'DUCATO', 2, 4.65, 2.13, 1.99);

INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (7, 'DAILY', 3, 4.9, 2.71, 1.9);
INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (8, 'DAILY 170 HIMATIC', 3, 6.99, 2.41, 2.01);
INSERT INTO public.vehicule_model( id, name, constructor_id, length, height, width) VALUES (9, 'DAILY 29 L H1', 3, 5.07, 2.26, 1.99);


/*************************************
* INSERTING DEFAULT PROFILE DOCUMENTS
*************************************/
INSERT INTO public.document(id, creation_date_time, hash, is_default, location, type)
	VALUES (-1, current_timestamp, '9dbb84cc15ce3e7b8a19c53410e3ea6be1118538e9419c2f9603716d4aba8584', true, '/file-storage/Images/default_man_avatar.jpg', 'IMAGE_JPEG');

INSERT INTO public.document(id, creation_date_time, hash, is_default, location, type)
	VALUES (-2, current_timestamp, '145149e1f9932c879bd90596f3b98d9fbdbfbe3426725c546101c7802d740b8b', true, '/file-storage/Images/default_woman_avatar.jpg', 'IMAGE_JPEG');

/*************************************
* INSERTING ENTITLEMENTS
*************************************/
INSERT INTO public.entitlement(id, read, type, write) VALUES (1, true, 'SUPPORT', true);
INSERT INTO public.entitlement(id, read, type, write) VALUES (2, true, 'OWNER', true);
INSERT INTO public.entitlement(id, read, type, write) VALUES (3, true, 'OTHERS', false);

INSERT INTO public.entitlement(id, read, type, write) VALUES (4, true, 'SUPPORT', true);
INSERT INTO public.entitlement(id, read, type, write) VALUES (5, true, 'OWNER', true);
INSERT INTO public.entitlement(id, read, type, write) VALUES (6, true, 'OTHERS', false);
/*****************************************
* INSERTING PROFILE DOCUMENTS ENTITLEMENTS
******************************************/
INSERT INTO public.document_entitlements(document_jpa_entity_id, entitlements_id) VALUES (-1, 1);
INSERT INTO public.document_entitlements(document_jpa_entity_id, entitlements_id) VALUES (-1, 3);

INSERT INTO public.document_entitlements(document_jpa_entity_id, entitlements_id) VALUES (-2, 4);
INSERT INTO public.document_entitlements(document_jpa_entity_id, entitlements_id) VALUES (-2, 6);

/*************************************
* INSERTING GENDERS
*************************************/
INSERT INTO public.gender(id, gender) VALUES (1, 'MAN');
INSERT INTO public.gender(id, gender) VALUES (2, 'WOMAN');

INSERT INTO public.localized_gender(id, locale, name) VALUES (1, 'en_US', 'Man');
INSERT INTO public.localized_gender(id, locale, name) VALUES (1, 'fr_FR', 'Homme');
INSERT INTO public.localized_gender(id, locale, name) VALUES (2, 'en_US', 'Woman');
INSERT INTO public.localized_gender(id, locale, name) VALUES (2, 'fr_FR', 'Femme');s


