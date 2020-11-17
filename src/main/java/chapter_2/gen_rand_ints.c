#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>

int main() {

    FILE *file = fopen("input.txt", "w");
    if(file == NULL)
    {
        printf("open error!\n");
        return 0;
    }
    long int max = 4 * ((long int)pow(10, 9));
    for (long int i = 0; i < max; i++) {
        int r = rand();
        char str[11]={0};
        sprintf(str,"%d", r);
        fputs(str, file);
        fputs("\n", file);
    }
    
    fclose(file);

}
