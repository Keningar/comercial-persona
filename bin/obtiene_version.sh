#! /bin/bash
for v in $1; do

    num=${v//./}
    let num++

    re=${v//./)(}
    re=${re//[0-9]/.}')'
    re=${re#*)}

    count=${v//[0-9]/}
    count=$(wc -c<<<$count)
    out=''
    for ((i=count-1;i>0;i--)) ; do
        out='.\'$i$out
    done

    sed -r s/$re$/$out/ <<<$num
done

