## Notes

## Nützliche Links 

- http://overpass-turbo.eu/
- https://material.io/components/
- http://wiki.openstreetmap.org/wiki/Overpass_API/Installation 
   - (müssen wir später auf unserem Server
   installieren und verwenden um die neuen Straßen aus unserer DBzu berücksichtigen)
- https://github.com/MKergall/osmbonuspack/wiki/Tutorial_3
    - Icon Clustering..
  
  
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
