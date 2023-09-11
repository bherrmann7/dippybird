
for each in `find /home/bob/dippy.dir/data -name body.html`
do
    echo tidy --break-before-br --wrap 200 -mi $each
    tidy -mi $each
    if [ $? = 2 ]
    then
	exit 1
    fi
done


echo All done.
