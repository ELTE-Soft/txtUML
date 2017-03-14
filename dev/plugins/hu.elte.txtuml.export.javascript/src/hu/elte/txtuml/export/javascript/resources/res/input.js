var input = {
   "classDiagrams" : [ {
      "name" : "hu.elte.txtuml.javascript.examples.creationdemo.diagrams.CDiagram",
      "attributeLinks" : [ {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.AB1",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.A",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B",
         "route" : [ {
            "x" : 156,
            "y" : 266
         } ],
         "type" : "normal",
         "from" : {
            "name" : "a",
            "multiplicity" : "1..5",
            "visibility" : "public",
            "navigable" : true,
            "composition" : false
         },
         "to" : {
            "name" : "b",
            "multiplicity" : "1",
            "visibility" : "public",
            "navigable" : true,
            "composition" : false
         },
         "name" : "AB1"
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.AB3",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.A",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B",
         "route" : [ {
            "x" : 182,
            "y" : 266
         } ],
         "type" : "normal",
         "from" : {
            "name" : "a",
            "multiplicity" : "1",
            "visibility" : "public",
            "navigable" : false,
            "composition" : false
         },
         "to" : {
            "name" : "b",
            "multiplicity" : "1",
            "visibility" : "public",
            "navigable" : true,
            "composition" : false
         },
         "name" : "AB3"
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.AB2",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.A",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B",
         "route" : [ {
            "x" : 195,
            "y" : 266
         } ],
         "type" : "normal",
         "from" : {
            "name" : "a",
            "multiplicity" : "0..1",
            "visibility" : "public",
            "navigable" : false,
            "composition" : false
         },
         "to" : {
            "name" : "b",
            "multiplicity" : "*",
            "visibility" : "public",
            "navigable" : true,
            "composition" : true
         },
         "name" : "AB2"
      } ],
      "nonAttributeLinks" : [ {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B#hu.elte.txtuml.javascript.examples.creationdemo.model.C",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.C",
         "route" : [ {
            "x" : 169,
            "y" : 588
         } ],
         "type" : "generalization"
      } ],
      "spacing" : 0.0,
      "classess" : [ {
         "position" : {
            "x" : 0,
            "y" : 0
         },
         "width" : 325,
         "height" : 154,
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.A",
         "name" : "A",
         "type" : "class",
         "attributes" : [ {
            "visibility" : "private",
            "name" : "_attr1",
            "type" : "Integer"
         }, {
            "visibility" : "public",
            "name" : "attr2",
            "type" : "String"
         }, {
            "visibility" : "protected",
            "name" : "attr3",
            "type" : "Boolean"
         } ],
         "operations" : [ {
            "visibility" : "protected",
            "name" : "function",
            "args" : [ {
               "name" : "arg",
               "type" : "Integer"
            } ],
            "returnType" : "Boolean"
         }, {
            "visibility" : "public",
            "name" : "A",
            "args" : [ ]
         } ]
      }, {
         "position" : {
            "x" : 117,
            "y" : 700
         },
         "width" : 91,
         "height" : 98,
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.C",
         "name" : "C",
         "type" : "class",
         "attributes" : [ ],
         "operations" : [ {
            "visibility" : "public",
            "name" : "C",
            "args" : [ ]
         } ]
      }, {
         "position" : {
            "x" : 117,
            "y" : 364
         },
         "width" : 91,
         "height" : 98,
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B",
         "name" : "B",
         "type" : "class",
         "attributes" : [ ],
         "operations" : [ {
            "visibility" : "public",
            "name" : "B",
            "args" : [ ]
         } ]
      } ]
   } ]
};