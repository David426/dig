#Reads from file 1, calls dig on each line, writes results in file 2
> $2
while read -r LINE; do
	echo "$LINE" >> $2
	for((i=0; i < 10; i++)) do 
		dig "$LINE" | grep "msec" | cut -c 4- >> $2
	done
done < $1




