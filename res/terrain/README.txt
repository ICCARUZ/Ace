Files in this folder are used by the data-driven terrain system to define different terrain types.
TO ENABLE A TERRAIN TYPE, INCLUDE THE NAME OF ITS DATA FILE IN THE TERRAINS_ACTIVE FILE
WARNING: DO NOT REPLACE THE OLD FILE OR EDIT ANY OF THE DEFAULT TERRAINS IN THE TERRAINS_ACTIVE FILE. THE LINE NUMBER OF EACH PRESENT TERRAIN IN THE ACTIVE FILE IS USED TO CONSTRUCT MAPS. BY CHANGING THE VALUES OF DEFAULT TERRAINS, YOU CORRUPT OLD MAPS. MAKE SURE TO APPEND NEW TERRAINS TO THE LIST RATHER THAN INSERTING THEM ELSEWHERE.

Attributes are assigned to terrain types by listing them in their files. 
If ommitted, attributes will be assigned their default values.
The sprite for each terrain type must be saved in the proper location with the same name as the terrain data file.

Defaults for all values are listed along with the identifiers and descriptions below.
For properties that are either on or off, simply adding the keyword to the data file will enable them. (A value can be specified, however.)
Other properties must be listed as keywords followed by their assigned values.

PROPERTIES:
solid: [0/1], if enabled will make a tile block line of sight for GROUND units - default 0
water: [0/1], if enabled allows only AIR or NAVAL units to cross or occupy a tile - default 0
beach: [0/1], if enabled allows LAND, AIR, or NAVAL units to occupy a tile - default 0
mountain: [0/1], if enabled allows only INFANTRY or AIR units to occupy a tile - default 0
vision: [INTEGER], grants a vision bonus or detriment to any GROUND unit occupying the tile - default 0
movepenalty: [INTEGER], an additional cost for GROUND units to cross a tile - default 0
movecost: [INTEGER], the base cost for a GROUND unit to cross a tile - default 1
defense: [0-4], the level of defense an occupying GROUND or NAVAL unit gains when occupying a tile - default 0