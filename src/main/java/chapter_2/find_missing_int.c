#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE_LENGTH 80
#define MAX_INT 0x7fffffff
#define RADIX 8



void add(char* bv, int i) {
    int idx = i/RADIX;
    int offset = i%RADIX;
    char mask = ((char)1) << (RADIX - 1 - offset);
    bv[idx] |= mask;
}

int contains(char* bv, int i) {
    int idx = i/RADIX;
    int offset = i%RADIX;
    char mask = ((char)1) << (RADIX - 1 - offset);
    return bv[idx] & mask;
}

/**
Got missing number: 6

real	15m49.054s
user	11m4.267s
sys	4m38.446s

*/

int main(int argc, char **argv)
{
    char *path = "input.txt";
    char line[MAX_LINE_LENGTH] = {0};
    unsigned int line_count = 0;
    char* bv = malloc(sizeof(char)*(MAX_INT/4 + 1));
    
    /* Open file */
    FILE *file = fopen(path, "r");
    
    if (!file)
    {
        perror(path);
        return EXIT_FAILURE;
    }
    
    /* Get each line until there are none left */
    while (fgets(line, MAX_LINE_LENGTH, file))
    {
        /* Print each line */
        //printf("line[%06d]: %s", ++line_count, line);

        if (strlen(line) > 1) {
            line[strlen(line)-1]  = '\0';
            int i = atoi(line);
            //printf("%d\n", i);
            add(bv, i);
        }
        
        /* In my case, all lines length is less than MAX_LINE_LENGTH. So below conditon won't happen.
        if (line[strlen(line) - 1] != '\n')
            printf("\n");
        */
    }
    
    /* Close file */
    if (fclose(file))
    {
        return EXIT_FAILURE;
        perror(path);
    }

    int missingInt = -1;
    for(int i = 0; i < MAX_INT; i++) {
        if (!contains(bv, i)) {
            missingInt = i;
            break;
        }
    }

    if (missingInt != -1) {
        printf("Got missing number: %d\n", missingInt);
    } else {
        printf("Not got missing number. Bad!");
    }


}
