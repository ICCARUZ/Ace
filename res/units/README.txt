!! THIS FEATURE IS CURRENTLY NOT IN USE !!

Files in this folder are used by the data-driven unit system to define different units.
TO ENABLE A UNIT, INCLUDE THE NAME OF ITS DATA FILE IN THE UNITS_ACTIVE FILE
WARNING: DO NOT REPLACE THE OLD FILE OR EDIT ANY OF THE DEFAULT UNITS IN THE UNITS_ACTIVE FILE. THE LINE NUMBER OF EACH PRESENT UNIT IN THE ACTIVE FILE IS USED TO CONSTRUCT MAPS. BY CHANGING THE VALUES OF DEFAULT UNITS, YOU CORRUPT OLD MAPS. MAKE SURE TO APPEND NEW UNITS TO THE LIST RATHER THAN INSERTING THEM ELSEWHERE.

Attributes are assigned to units by listing them in their files. 
If ommitted, attributes will be assigned their default values.
The sprites for each unit must be saved in the proper location with the same name as the unit's data file.

Defaults for all values are listed along with the identifiers and descriptions below.
For properties that are either on or off, simply adding the keyword to the data file will enable them. (A value can be specified, however.)
Other properties must be listed as keywords followed by their assigned values.

PROPERTIES:
hp: [INTEGER], the maximum hit points of the unit - default 10
type: [infantry/mech/naval/air], a unit's unit type, used to determine combat weaknesses and where it can travel - default infantry
mechbuster: [0/1], indicates whether or not a unit performs better when attacking mech, naval, and air targets - default 0
peashooter: [0/1], indicates whether or not a unit performs worse when attacking mech, naval, and air targets - default 0
vision: [INTEGER], vision radius in fog of war - default 5
move: [INTEGER], maximum distance that can be traveled in one turn - default 4
attack: [INTEGER], attack value, used to calculate combat damage - default 2
defense: [INTEGER], defense value, used to calculate combat damage - default 0