<?php

function encode($latitude, $Longitude, $geohashLength = 5){
    
    function getBits($coordinate, $min, $max, $bitsLength){
        $binaryString = "";
        $i = 0;
        while ($bitsLength > $i) {
            $mid = ($min+$max)/2;
            if ($coordinate > $mid) {
                $binaryString .= "1";
                $min = $mid;
            } else {
                $binaryString .= "0";
                $max = $mid;
            }
            $i++;
        }
        return $binaryString;
    }

    $base32Mapping = "0123456789bcdefghjkmnpqrstuvwxyz";
    // Get latitude and longitude bits length from given geohash Length
    if ($geohashLength % 2 == 0) {
        $latBitsLength = $lonBitsLength = ($geohashLength/2) * 5;
    } else {
        $latBitsLength = (ceil($geohashLength / 2) * 5) - 3;
        $lonBitsLength = $latBitsLength + 1;
    }
    // Convert the coordinates into binary format
    $binaryString = "";
    $latbits = getBits($latitude, -90, 90, $latBitsLength);
    $lonbits = getBits($Longitude, -180, 180, $lonBitsLength);
    $binaryLength = strlen($latbits) + strlen($lonbits);
    // Combine the lat and lon bits and get the binaryString
    for ($i=1 ; $i < $binaryLength + 1; $i++) {
        if ($i%2 == 0) {
            $pos = (int)($i-2)/2;
            $binaryString .= $latbits[$pos];
        } else {
            $pos = (int)floor($i/2);
            $binaryString .= $lonbits[$pos];
        }
    }
    // Convert the binary to hash
    $hash = "";
    for ($i=0; $i< strlen($binaryString); $i+=5) {
        $n = bindec(substr($binaryString,$i,5));
        $hash = $hash . $base32Mapping[$n];
    }
    return $hash;
}

?>