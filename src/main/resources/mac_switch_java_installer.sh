#!/usr/bin/env bash

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
cat ${CURRENT_DIR}/mac_switch_java_installer.sh | tail -43 > ~/.my_cmd
echo "source ~/.my_cmd" >> /etc/profile
export NO_JDK_PATH=$PATH
echo "Command: use"
echo "Version: 1.0"
echo "***************************"
echo "***** Install Success *****"
echo "***************************"
use() {
    JDK_HOME_PRE=/Library/Java/JavaVirtualMachines/

    declare -a jdkHomes

    for jdk in `ls ${JDK_HOME_PRE}`
    do
        if [[ ${jdk} = *"1.6"* ]] || [[ ${jdk} = *"6"* ]]
        then
            jdkHomes["6"]=${JDK_HOME_PRE}${jdk}/Contents/Home
        elif [[ ${jdk} = *"1.7"* ]] || [[ ${jdk} = *"7"* ]]
        then
            jdkHomes["7"]=${JDK_HOME_PRE}${jdk}/Contents/Home
        elif [[ ${jdk} = *"1.8"* ]] || [[ ${jdk} = *"8"* ]]
        then
            jdkHomes["8"]=${JDK_HOME_PRE}${jdk}/Contents/Home
        elif [[ ${jdk} = *"1.9"* ]] || [[ ${jdk} = *"9"* ]]
        then
            jdkHomes["9"]=${JDK_HOME_PRE}${jdk}/Contents/Home
        elif [[ ${jdk} = *"10"* ]]
        then
            jdkHomes["10"]=${JDK_HOME_PRE}${jdk}/Contents/Home
        elif [[ ${jdk} = *"11"* ]]
        then
            jdkHomes["11"]=${JDK_HOME_PRE}${jdk}/Contents/Home
        fi
    done

    if [[ $1 = "java" ]]
    then
        JAVA_HOME=${jdkHomes["$2"]}
        if [[ ${JAVA_HOME} = *"$2"*} ]]
        then
            export PATH=${NO_JDK_PATH}:${JAVA_HOME}/bin
            echo "switch to java $2"
        else
            echo "you have not install java $2, please try install it first"
        fi
    else
        echo "usage use java [java-version] only support 6+, i.e. use java 6"
    fi
}