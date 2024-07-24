## Dejero Sockets (Jason Wilson)
Welcome to my sockets project. 

### Assumptions
Due to my lack on knowledge and experience with TLV-blobs, I've tried to implement a program that
takes the example TLV-blob and convert it to as close as the expected output as possible.
```
E11000000000DA7A0000000501020304050B1E00000000
---mapping to---
[127.0.0.1:5678] [Hello] [0] []
[127.0.0.1:5678] [Data] [5] [0x01 0x02 0x03 0x04] [127.0.0.1:5678] [Goodbye] [0] []
```
#### Assumption 1 (Types)
`E110`, `DA7A` and `0B1E` are always in the blob. If one of these values are missing, the application will
throw an error. Any other values are not expected as I wasn't sure how the Hex values line up to their respective
sample output values. (eg: E110 == Hello? etc)

#### Assumption 2 (Length)
The length for each blob is the range of chars from index 2 through 5 (4-digit value). I wasn't sure how those
4-digits lines up to length in the `DA7A` blob entry. For example, if we expect `DA7A000000050102030405` to be of length 5,
we need to take the value of the length digits `0000` and convert that to somehow get 5. 

So what I've done was just convert that length value from Hex to decimal and grabbed the length of the decimal value. And 
if the value of said blob contains all zeros (that is its hex value is also equal to 0), we overwrite the length value to be
zero also. This seemed to line up with the assumption that both TLV-blobs with a value containing all zeros (E110 and 0B1E)
having a length of `0` in the sample output. I'm not sure this logic is correct but once I learn the TLV conversion logic 
I could implement that in the parser. I just need more context.

#### Assumption 3 (Value)
I assume the value is the value in each TLV-blob from index 6 to the next found blob index. I say that only because I coudln't
figure out how the length section of the TLV-blob corresponded with the value section since the value section was longer
than the computed length by a lot. So I just parsed the string by the expected key values (`E110`, `DA7A`, `0B1E`). From there
I just gathered the first X digits (x == computed length value above) of the values array and output them to a string array
containing their respective value and index. (eg: `DA7A` has a value string of `000000050102030405` so I just grabbed
the first 5 chars since the length computed was 5 regardless of the remaining values in the byte array).

### Executing the Program
To run the application locally, find the JAR, cd to that directory and run: `java -jar dejero-sockets.jar [PORT]`. The
program executes until a `CRTL^C` sig-int occurs. **Note there is no param for IP address since this is running on localhost**.

From here you can make calls to the application like so: `echo 'E11000000000DA7A0000000501020304050B1E00000000' | nc -p 5678 localhost 6666`

**Note the lack of the encryption pipe `xxd -r -p`**. I could implement a way to decrypt the string but that would take more research
and simply ran out of time.

There is a bash script [ping-sockets.sh](ping-sockets.sh) that executes 3 concurrent calls from different client sockets to
our application and writes data appropriately to a txt files for each successful execution

### Additional Notes
Areas I have not implemented are:
- cannot pass `xxd -r -p` as an arg because I did not have time to implement a decryption method Java but with more time
and experience could do. **If we pass in this pipe the program will fail**.
- We cannot accept blobs that have type values other that `E110`, `Da7A` and `0B1E`
- If all 3 of the expected values stated above are not present, the application will through a runtime exception.
- Data is printed to `.txt.` files per transaction in the format it is printed to console (not JSON unfortunately).
- We never got around to accepting TLV blob history part
- As far as checking for malicious data goes, I just implemented a quick string checker to look for generic malicious String
values such as `<html>`, `INSERT` etc.