#! /usr/bin/bash

# Program Starts here
PROJDIR=$HOME/AOS/VotingProtocol

# config file name
config_file=$PROJDIR/config.txt
echo "started"
num_nodes=`grep -A 1 -i '#Number of nodes' $config_file | tail -1`
echo $num_nodes
num_files=`grep -A 1 -i '#Number of files in the system' $config_file | tail -1`


echo $num_files
file_names=`ls ~/AOS/f*.txt`
file_count=`echo $file_names | wc -w`
echo "check"
if [ $num_files -ne $file_count ]; then
	echo "WARNING: given number of files in config file and actual count mismatch."
	echo "Actaul File count: $file_count"
	echo "Actaul File count: $file_count"
fi
echo "here"
i=1
while [ $i -le $num_nodes ]
do
	dir_name="n"$i
	mkdir -p $dir_name
	ret=$?
	if [ $ret -ne 0 ]; then
		echo "Failed to create director $dir_name"
	fi

	for file in $file_names
	do
		cp $file $dir_name
	done
	i=$((i+1))
done
cd /home/004/b/bx/bxp131030/AOS/VP
for f in *.java
do
	#echo $f
	cp $f /home/004/b/bx/bxp131030/AOS/VotingProtocol/$f
done
