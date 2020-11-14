/*
 * C Program to Find the Number of Lines in a Text File
 */
#include <stdio.h>
 
/*

handle a file about 40G and 4*10^9 lines.
real	3m26.356s
user	1m16.178s
sys	2m5.630s

*/
int main()
{
    FILE *fileptr;
    long count_lines = 0;
    char filechar[40], chr;
 
    printf("Enter file name: ");
    scanf("%s", filechar);
    fileptr = fopen(filechar, "r");
   //extract character from file and store in chr
    chr = getc(fileptr);
    while (chr != EOF)
    {
        //Count whenever new line is encountered
        if (chr == '\n')
        {
            count_lines = count_lines + 1;
        }
        //take next character from file.
        chr = getc(fileptr);
    }
    fclose(fileptr); //close file.
    printf("There are %ld lines in %s  in a file\n", count_lines, filechar);
    return 0;
}
