#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
//math.h needs cc source.c -lm
#include <math.h>

void binary_search(int first, int last) {
    if (first <= last) {
        double mid = (first+last)/2.0;
        //printf("%f\n", mid) ;
        size_t len;
        char *str;

        len = (size_t)snprintf(NULL, 0, "%f", mid) + 1;
        str = malloc(len);
        snprintf(str, len, "%f", mid);
        //puts(str);
        printf("str: %s\n", str);
        free(str);
        
        if (floor(mid) == mid) {
            int midInt = (int)mid;
            binary_search(first, midInt - 1);
            binary_search(midInt + 1, last);
        } else{
            binary_search(first, (int)floor(mid));
            binary_search((int)ceil(mid), last);
        }
    }
}

int main()
{
   int i;
   char buf[12];

/*
   for (i = 0; i < 100; i++) {
      snprintf(buf, 12, "pre_%d_suff", i); // puts string into buffer
      printf("%s\n", buf); // outputs so you can see it
   }
   */
  binary_search(0, INT_MAX);

}