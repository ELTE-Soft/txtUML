var input = {
   "classDiagrams" : [ {
      "name" : "hu.elte.txtuml.javascript.examples.creationdemo.diagrams.CDiagram",
      "classes" : [ {
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
      }, {
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
      } ],
      "attributeLinks" : [ {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.AB3",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.A",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B",
         "route" : [ {
            "x" : 156,
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
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.AB1",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.A",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B",
         "route" : [ {
            "x" : 182,
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
      "spacing" : 0.0
   } ],
   "stateMachines" : [ {
      "name" : "hu.elte.txtuml.javascript.examples.creationdemo.diagrams.SMDiagram",
      "machineName" : "B",
      "states" : [ {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S2",
         "name" : "S2",
         "position" : {
            "x" : 102,
            "y" : 6
         },
         "width" : 48,
         "height" : 51
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S1",
         "name" : "S1",
         "position" : {
            "x" : 0,
            "y" : 6
         },
         "width" : 48,
         "height" : 51
      } ],
      "pseudoStates" : [ {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.C",
         "name" : "C",
         "kind" : "choice",
         "position" : {
            "x" : 213,
            "y" : 18
         },
         "width" : 27,
         "height" : 27
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.Init",
         "name" : "Init",
         "kind" : "initial",
         "position" : {
            "x" : 21,
            "y" : 135
         },
         "width" : 25,
         "height" : 25
      } ],
      "transitions" : [ {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.T1_C",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S1",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.C",
         "trigger" : "Signal1",
         "route" : [ {
            "x" : 42,
            "y" : 0
         }, {
            "x" : 216,
            "y" : 0
         } ]
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.TC_2",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.C",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S2",
         "route" : [ {
            "x" : 180,
            "y" : 39
         } ]
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.TC_1",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.C",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S1",
         "route" : [ {
            "x" : 216,
            "y" : 60
         }, {
            "x" : 45,
            "y" : 60
         } ]
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.T1_2",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S1",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S2",
         "trigger" : "Signal2",
         "route" : [ {
            "x" : 75,
            "y" : 33
         } ]
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.TI_S1",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.Init",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S1",
         "route" : [ {
            "x" : 24,
            "y" : 96
         } ]
      }, {
         "id" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.T2_1",
         "fromID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S2",
         "toID" : "hu.elte.txtuml.javascript.examples.creationdemo.model.B.S1",
         "trigger" : "Signal2",
         "route" : [ {
            "x" : 105,
            "y" : 3
         }, {
            "x" : 45,
            "y" : 3
         } ]
      } ],
      "spacing" : 0.0
   } ]
};