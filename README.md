## BPCE-TEST MEETING PLANNER : Gestionnaire des salles de réunion d'une PME

Ce projet est une API développé en Java 17. Les librairies utilisées sont:
	- spring-boot (avec web et data-jpa)
	- lombok
	- h2database

Le but est de pouvoir faire une réservation de salle pour un meeting au sein d'une entreprise.
Les données sont sur une base H2 en mémoire accéssible via une interface web (login: sa et sans mdp)
Au lancement de l'application le fichier `data.sql` est éxécuté pour créer la table `ROOM` et y insérer les données présentes dans le fichier PDF de l'énnoncé.
## MODEL DE DONNEES
### ROOM
| Champ | Type     | Description                                          |
|---------------|----------|------------------------------------------------------|
| `id`          | `Long`   | Identifiant unique pour la room.                     |
| `name`        | `String` | Nom de la Room.                               |
| `capacity`    | `Int`    | Maximum de personnes pouvant être présentes.   |
| `ecran`       | `Boolean`| Indique si la room dispose d'un ecran.            |
| `pieuvre`     | `Boolean`| Idique si la room dispose d'une pieuvre.|
| `webcam`      | `Boolean`| Indicates si la room dispose d'une webcam.            |
| `tableau`     | `Boolean`| Indicates si la room dispose d'un tableau.        |

### MEETING
| Field         | Type       | Description                                                |
|---------------|------------|------------------------------------------------------------|
| `id`          | `Long`     |Identifiant unique pour le meeting.                         |
| `type`        | `String`   | Type de meeting  (VC, SPEC, RS, RC).                  |
| `participants`| `Int`      | Nombre de participants au meeting.                     |
| `startTime`   | `LocalTime`| Heure de début du  meeting.                          |
| `endTime`     | `LocalTime`| Heure de fin du meeting.                            |
| `room`        | `Room`     | Relation entre la room et le meeting |

## ENPOINTS
### Meeting endpoints (/meetings):
1. **GET** retourne la liste de tous les meetings planifiés.
- **paramètres** (tous falcultatifs):
		1. roomName
		2. startTime
		3. endTime
	exemple: `/meetings?startTime=08:00&roomName=E3001`
	**response**:
```
[
	{
	"id": 1,
	"type": "VC",
	"participants": 8,
	"startTime": "10:00:00",
	"endTime": "11:00:00",
	"room": {
		"id": 9,
		"name": "E3001",
		"capacity": 13,
		"ecran": true,
		"pieuvre": true,
		"webcam": true,
		"tableau": false
		}
	}
]
```
	
2. **POST** /plan
- **Description** : Planifie une ou plusieurs réunions en attribuant une salle disponible pour chaque réunion.
- **Request Body** :
  - `meetingDtos` : Liste d'objets `MeetingDto` contenant les informations des réunions à planifier.
```
[
  {
    "type": "VC",
    "participants": 8,
    "startTime": "08:00:00"
  },
  {
    "type": "SPEC",
    "participants": 4,
    "startTime": "09:00:00"
  }
]
```
**response**:
```
{"scheduledMeetings":
[
	{
	"id":1,
	"type":"VC",
	"participants":8,
	"startTime":"08:00:00",
	"endTime":"09:00:00",
	"room: {
		"id":9,
		"name":"E3001",
		"capacity":13,
		"ecran":true,
		"pieuvre":true,
		"webcam":true,
		"tableau":false
		}
},
{
	"id":2,
	"type":"SPEC",
	"participants":4,
	"startTime":"09:00:00",
	"endTime":"10:00:00",
	"room":{
		"id":4,
		"name":"E1004",
		"capacity":4,
		"ecran":false,
		"pieuvre":false,
		"webcam":false,
		"tableau":true
		}
}
],
"errorMeetingDtos":[]
}
```
3. **POST** /plan-single
- **Description** : Planifie une  réunion en attribuant une salle disponible pour la réunion.
- **Request Body** :
  - `meetingDto` : Un`MeetingDto` contenant les informations des réunions à planifier.
```
{
	"type": "VC",
	"participants": 8,
	"startTime": "10:00:00"
}
```
**response**:
```
{
	"id": 3,
	"type": "VC",
	"participants": 8,
	"startTime": "10:00:00",
	"endTime": "11:00:00",
	"room": {
		"id": 9,
		"name": "E3001",
		"capacity": 13,
		"ecran": true,
		"pieuvre": true,
		"webcam": true,
		"tableau": false
	}
}
```
### ROOM endpoint
1. **GET** retourne la liste de tous les meetings planifiés.
	exemple: `/rooms`
	**response**:
```
[
	{
	"id": 1,
	"name": "E1001",
	"capacity": 23,
	"ecran": false,
	"pieuvre": false,
	"webcam": false,
	"tableau": false
	},
	{
	"id": 2,
	"name": "E1002",
	"capacity": 10,
	"ecran": true,
	"pieuvre": false,
	"webcam": false,
	"tableau": false
	},
...
]
```

### Body à mettre dans la requête pour la liste des réunions attendues:
```
[   
    {
    "type": "VC",
    "participants": 8,
    "startTime": "09:00"
    },
    {
    "type": "VC",
    "participants": 6,
    "startTime": "09:00"
    },
    {
    "type": "RC",
    "participants": 4,
    "startTime": "11:00"
    },
    {
    "type": "RS",
    "participants": 2,
    "startTime": "11:00"
    },
    {
    "type": "SPEC",
    "participants": 9,
    "startTime": "11:00"
    },
    {
    "type": "RC",
    "participants": 7,
    "startTime": "09:00"
    },
    {
    "type": "VC",
    "participants": 9,
    "startTime": "08:00"
    },
    {
    "type": "SPEC",
    "participants": 10,
    "startTime": "08:00"
    },
    {
    "type": "SPEC",
    "participants": 5,
    "startTime": "09:00"
    },
    {
    "type": "RS",
    "participants": 4,
    "startTime": "09:00"
    },
    {
    "type": "RC",
    "participants": 8,
    "startTime": "09:00"
    },
    {
    "type": "VC",
    "participants": 12,
    "startTime": "11:00"
    },
    {
    "type": "SPEC",
    "participants": 5,
    "startTime": "11:00"
    },
    {
    "type": "VC",
    "participants": 3,
    "startTime": "08:00"
    },
    {
    "type": "SPEC",
    "participants": 2,
    "startTime": "08:00"
    },
    {
    "type": "VC",
    "participants": 12,
    "startTime": "08:00"
    },
    {
    "type": "VC",
    "participants": 6,
    "startTime": "10:00"
    },
    {
    "type": "RS",
    "participants": 2,
    "startTime": "11:00"
    },
    {
    "type": "RS",
    "participants": 4,
    "startTime": "09:00"
    },
    {
    "type": "RS",
    "participants": 7,
    "startTime": "09:00"
    }
]
```
