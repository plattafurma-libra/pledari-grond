The conjugation process moved to the maven module 'maalr.conjugation'. The classes inside this 
package are kept for historical reasons.

To declare which 'VerbClassGenerator' is used for the conjugation process, you will have
to adjust the package declaration inside the 'maalr_xy_config/overlays/conjugation-editor.xml'. 
There is the so called 'generator' attribute within the 'presetchooser' element. Just paste 
there the full package path to 'VerbClassGenerator'.