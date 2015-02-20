package txtuml.examples.example1;

import txtuml.api.layout.*;
import txtuml.api.layout.elements.*;
import txtuml.api.layout.statements.*;

class G implements LayoutGroup {}
class A implements LayoutNode {}
class B implements LayoutNode {}
class P implements LayoutLink {}

@North(value = A.class, from = B.class)
@South(value = A.class, from = B.class)
@East(value = A.class, from = B.class)
@West(value = A.class, from = B.class)
@Above(value = A.class, from = B.class)
@Below(value = A.class, from = B.class)
@Right(value = A.class, from = B.class)
@Left(value = A.class, from = B.class)
@TopMost(A.class)
@BottomMost(A.class)
@RightMost(A.class)
@LeftMost(A.class)
@InGroup(value = A.class, group = G.class)
@InGroup(value = B.class, group = G.class)
@GroupLayout(value = G.class, layout = GroupLayoutType.TopToBottom)
@GroupLayout(value = G.class, layout = GroupLayoutType.BottomToTop)
@GroupLayout(value = G.class, layout = GroupLayoutType.RightToLeft)
@GroupLayout(value = G.class, layout = GroupLayoutType.LeftToRight)
@Priority(value = P.class, prior = 100)
@North(value = P.class, from = B.class)
@South(value = P.class, from = B.class)
@East(value = P.class, from = B.class)
@West(value = P.class, from = B.class)
@InGroup(value = P.class, group = G.class)
public class Example1Layout extends Layout {}
