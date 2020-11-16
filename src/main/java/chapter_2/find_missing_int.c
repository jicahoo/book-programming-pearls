#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE_LENGTH 80

int main(int argc, char **argv)
{
    char *path = "input-small.txt";
    char line[MAX_LINE_LENGTH] = {0};
    unsigned int line_count = 0;
    char[] bv = malloc(0x7fffffff);
    
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
        printf("line[%06d]: %s", ++line_count, line);

        if (strlen(line) > 1) {
            line[strlen(line)-1]  = '\0';
            int i = atoi(line);
            printf("%d\n", i);
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
}
