#!/bin/bash

for i in `find . -name '*.kt'`; do 
  if [[ `cat $i | grep 'Freeware License' | wc -l` = 0 ]]; then
    echo $i
    echo '/*' > ujm7
    cat README.license.txt >> ujm7
    echo '*/' >> ujm7
    cat $i >> ujm7
    mv ujm7 $i
  fi
done

