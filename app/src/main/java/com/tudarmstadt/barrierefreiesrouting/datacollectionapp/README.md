## Notes

## Nützliche Links 

- http://overpass-turbo.eu/
- https://stackoverflow.com/questions/20322823/how-to-get-all-roads-around-a-given-location-in-openstreetmap
- https://material.io/components/
- http://wiki.openstreetmap.org/wiki/Overpass_API/Installation 
   - (müssen wir später auf unserem Server
   installieren und verwenden um die neuen Straßen aus unserer DBzu berücksichtigen)
   
  
  
## Example Overpass API Call
   
   (
     way
     (around:100,49.8728,8.6512)
     [highway~"^(primary|secondary|tertiary|residential|footway|bridleway)$"]
     [name];
   >;);out;


(
  way
  (around:400,49.8728,8.6512)
  [highway~"^(.*)$"]
  [name];
>;);out;
