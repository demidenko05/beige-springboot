#!/bin/bash
   
# It generates simple getters and setters for given .java file.
# You have to respect checkstyle, e.g. "No tabs"
echo "  //Simple getters and setters:"
cat $1 | grep private | awk 'BEGIN { FS = "  | |;" }
{
  varType = "1"
  varName = ""
  for (i = 1; i <= NF; i++) if ($i != "" && $i != "private") {
    if (varType == "1") varType = $i;
    else {
      if (index($i, ",") > 0 || index($i, ">") > 0) varType = varType " " $i;
      else { varName = $i; break; }
    }
  }
  if (varName != "") {
    print "" ;
    print "  /**" ;
    print "   * <p>Getter for " varName ".</p>" ;
    print "   * @return " varType;
    print "   **/" ;
    print "  public final " varType " get" toupper(substr(varName,1,1)) substr(varName, 2) "() {";
    print "    return this." varName ";";
    print "  }";
    print "" ;
    print "  /**" ;
    print "   * <p>Setter for " varName ".</p>" ;
    print "   * @param p" toupper(substr(varName,1,1)) substr(varName, 2) " reference";
    print "   **/" ;
    print "  public final void set" toupper(substr(varName,1,1)) substr(varName, 2) "(final " varType " p" toupper(substr(varName,1,1)) substr(varName, 2) ") {";
    print "    this." varName " = p" toupper(substr(varName,1,1)) substr(varName, 2) ";";
    print "  }";
  }
}'
