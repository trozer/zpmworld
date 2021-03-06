package zpmworld;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.Serializable;
import java.awt.Point;

public class Stage implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int allZPM;
    private List<Unit> units;
    private List<ZPM> zpms;
    private List<Field> fields;
    private List<Field> roads;
    private Portal portal;
    private boolean log;
    private List<String> lastLog;
	private Game game;


    public Stage (File file, Game game) throws Exception {
        units =  new ArrayList<Unit>();
        fields = new ArrayList<Field>();
        roads = new ArrayList<Field>();
        zpms = new ArrayList<ZPM>();
        lastLog = new ArrayList<String>();
        log = true;
        portal = null;
        allZPM = 0;     //játék kezdetén lévő zpm-ek darabszáma
		this.game = game;
        init(file, game);
    }

    public void init(File stage, Game game) throws Exception {
        portal = new Portal();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(stage);
        doc.getDocumentElement().normalize();
        NodeList fieldRows = doc.getElementsByTagName("row");

        List<List<Field>> buildFields = new ArrayList<List<Field>>();
        List<Scale> connectScales = new ArrayList<Scale>();
        List<Gate> connectGates = new ArrayList<Gate>();
        List<PortalWall> activatePortalWalls = new ArrayList<PortalWall>();

        //read fields row by row
        for (int i = 0; i < fieldRows.getLength(); i++) {
            String row = fieldRows.item(i).getTextContent();
            buildFields.add(new ArrayList<Field>());

            for (int j = 0; j < row.length(); j++) {
                Field field;
                switch (row.charAt(j)) {
                    case 'r':
                        Road rtemp = new Road();
                        field = rtemp;
                        roads.add(field);
                        game.registerDrawableField(new DrawableRoad(rtemp));
                        break;
                    case 'a':
                        Abyss atemp = new Abyss();
                        field = atemp;
                        game.registerDrawableField(new DrawableAbyss(atemp));
                        break;
                    case 'w':
                        Wall rwall = new Wall();
                        field = rwall;
                        game.registerDrawableField(new DrawableWall(rwall));
                        break;
                    case 'p':
                        PortalWall portalWall = new PortalWall(portal);
                        field = portalWall;
                        activatePortalWalls.add(portalWall);
                        game.registerDrawableField(new DrawablePortalWall(portalWall));
                        break;
                    case 's':
                        Scale scale = new Scale();
                        field = scale;
                        connectScales.add(scale);
                        game.registerDrawableField(new DrawableScale(scale));
                        break;
                    case 'g':
                        Gate gate = new Gate();
                        field = gate;
                        connectGates.add(gate);
                        game.registerDrawableField(new DrawableGate(gate));
                        break;
                    default:
                        throw new Exception("Hiba: �rv�nytelen karakter a t�blale�r� r�szben");
                }
                buildFields.get(i).add(field);
            }
        }

        //set neighbours
        for (int i = 0; i < buildFields.size(); i++) {
            for (int j = 0; j < buildFields.get(i).size(); j++) {
                if ((j - 1) >= 0) {
                    buildFields.get(i).get(j).addNeighbour(Direction.WEST,
                            buildFields.get(i).get(j - 1));
                }
                if ((j + 1) < buildFields.get(i).size()) {
                    buildFields.get(i).get(j).addNeighbour(Direction.EAST,
                            buildFields.get(i).get(j + 1));
                }
                if ((i - 1) >= 0) {
                    buildFields.get(i).get(j).addNeighbour(Direction.NORTH,
                            buildFields.get(i - 1).get(j));
                }
                if ((i + 1) < buildFields.size()) {
                    buildFields.get(i).get(j).addNeighbour(Direction.SOUTH,
                            buildFields.get(i + 1).get(j));
                }
                buildFields.get(i).get(j).setPosition(new Point(j, i));
                fields.add(buildFields.get(i).get(j));
            }
        }

        //make connection between scales and gates
        NodeList nConnections = doc.getElementsByTagName("connection");

        for (int i = 0; i < nConnections.getLength(); i++) {
            Node nConnection = nConnections.item(i);
            if (nConnection.getNodeType() == Node.ELEMENT_NODE) {
                Element connectionElement = (Element) nConnection;

                Point fromPos = new Point(Integer.parseInt(connectionElement.getAttribute("fCol")),
                        Integer.parseInt(connectionElement.getAttribute("fRow")));
                Point toPos = new Point(Integer.parseInt(connectionElement.getAttribute("toCol")),
                        Integer.parseInt(connectionElement.getAttribute("toRow")));
                for (Scale scale : connectScales) {
                    for (Gate gate : connectGates) {
                        if (scale.getPosition().equals(fromPos) &&
                                gate.getPosition().equals(toPos)) {
                            scale.setGate(gate);
                        }
                    }
                }
            }
        }


        //activate portalwalls
        NodeList nPortalWallColors = doc.getElementsByTagName("portalwall_color");

        for (int i = 0; i < nPortalWallColors.getLength(); i++) {
            Node nPortalWallColor = nPortalWallColors.item(i);
            if (nPortalWallColor.getNodeType() == Node.ELEMENT_NODE) {
                Element portalWallColorElement = (Element) nPortalWallColor;

                Point Pos = new Point(Integer.parseInt(portalWallColorElement.getAttribute("col")),
                        Integer.parseInt(portalWallColorElement.getAttribute("row")));
                Color color = colorByString(portalWallColorElement.getAttribute("color"));
                for (PortalWall portalWall : activatePortalWalls) {
                    if (portalWall.getPosition().equals(Pos)) {
                        portal.createPortal(portalWall, color);
                    }
                }
            }
        }

        NodeList nUnits = doc.getElementsByTagName("unit");
        //add units
        Player oneill = null;
        Player jaffa = null;
        List<ZPM> ZPMsToCheck = new ArrayList<ZPM>();
        List<Box> boxesToCheck = new ArrayList<Box>();
        for (Field field : fields) {
            for (int i = 0; i < nUnits.getLength(); i++) {
                Node nUnit = nUnits.item(i);
                if (nUnit.getNodeType() == Node.ELEMENT_NODE) {
                    Element unitElement = (Element) nUnit;

                    Point unitPos = new Point(Integer.parseInt(unitElement.getAttribute("col")),
                            Integer.parseInt(unitElement.getAttribute("row")));

                    if (field.getPosition().equals(unitPos)) {
                        String unitType = unitElement.getTextContent();
                        if (unitType.equals("Bullet")) {
                            Color color = colorByString(unitElement.getAttribute("color"));
                            Direction dir = directionByChar(unitElement.getAttribute("direction").charAt(0));
                            Bullet bullet = new Bullet(new Action(ActionType.MOVE, dir, color), field);
                            units.add(bullet);
                            field.addUnit(bullet);
                            game.registerDrawableUnit(new DrawableBullet(bullet));
                        } else if (unitType.equals("O'neill") || unitType.equals("Jaffa")) {
                            Direction dir = directionByChar(unitElement.getAttribute("direction").charAt(0));
                            ActionType actionType = actionTypeByString(unitElement.getAttribute("action"));
                            Direction turnDir = Direction.NORTH; //default, this is not affect actions (exclude turn)
                            List<ZPM> playerZPMs = new ArrayList<ZPM>();
                            String numStr = unitElement.getAttribute("zpm");
                            int numOfZPMs;
                            if(numStr == null || numStr == "" || numStr == "0"){
                                numOfZPMs = 0;
                            } else {
                                numOfZPMs = Integer.valueOf(numStr);
                                for(int j = 0; j < numOfZPMs; ++j){
                                    ZPM zpm = new ZPM(null);
                                    units.add(zpm);
                                    playerZPMs.add(zpm);
                                }
                            }
                            Color color = null;
                            Box box = null;
                            if (actionType == ActionType.TURN)
                                turnDir = directionByChar(unitElement.getAttribute("turndir").charAt(0));
                            if (actionType == ActionType.SHOOT)
                                color = colorByString(unitElement.getAttribute("color"));
                            if (unitElement.getAttribute("box").equals("true")) {
                                box = new Box(null);    //eleve fel lesz véve, így a currentFieldje null
                                units.add(box);
                                game.registerDrawableUnit(new DrawableBox(box));    // be is regisztráljuk, hogy ki lehessen rajzolni
                            }

                            if (unitType.equals("O'neill")) {
                                oneill = new Player(allZPM, dir, new Action(actionType, turnDir, color), field, game, box, "O'neill", playerZPMs);
                                game.setOneill(oneill);
                                game.registerDrawableUnit(new DrawablePlayer(oneill));
                                units.add(oneill);
                                field.addUnit(oneill);
                            } else {
                                jaffa = new Player(allZPM, dir, new Action(actionType, turnDir, color), field, game, box, "Jaffa", playerZPMs);
                                game.setJaffa(jaffa);
                                game.registerDrawableUnit(new DrawablePlayer(jaffa));
                                units.add(jaffa);
                                field.addUnit(jaffa);
                            }
                        } else if (unitType.equals("Replicator")) {
                            Direction dir = directionByChar(unitElement.getAttribute("direction").charAt(0));
                            Replicator replicator = new Replicator(game, dir, field);
                            units.add(replicator);
                            field.addUnit(replicator);
                            game.registerDrawableUnit(new DrawableReplicator(replicator));
                            game.setReplicator(replicator);
                        } else if (unitType.equals("Box")) {
                            Box box = new Box(field);
                            units.add(box);
                            boxesToCheck.add(box);
                            game.registerDrawableUnit(new DrawableBox(box));
                            if (!connectGates.contains(field))
                                field.addUnit(box);
                        } else if (unitType.equals("ZPM")) {
                            allZPM++;
                            ZPM zpm = new ZPM(field);
                            field.addUnit(zpm);
                            game.registerDrawableUnit(new DrawableZPM(zpm));
                            units.add(zpm);
                            zpms.add(zpm);
                            ZPMsToCheck.add(zpm);
                        } else
                            throw new Exception("Hiba: ismeretlen egys�gt�pus");
                    }
                }
            }
        }

        // ellenőrizni, hogy Oneill vagy Jaffa alá nincs-e berakva ZPM, vagy Box
        if (!ZPMsToCheck.isEmpty()) {
            for (ZPM zpm : ZPMsToCheck) {
                if (oneill != null && zpm.getCurrentField().getPosition().x == oneill.getCurrentField().getPosition().x && zpm.getCurrentField().getPosition().y == oneill.getCurrentField().getPosition().y) { //ha egy mezőn állnak
                    zpm.getCurrentField().removeUnit(zpm);
                    zpm.setCurrentField(null);
                    oneill.addZPMSimply(zpm);
                } else if (jaffa != null && zpm.getCurrentField().getPosition().x == jaffa.getCurrentField().getPosition().x && zpm.getCurrentField().getPosition().y == jaffa.getCurrentField().getPosition().y) {
                    zpm.getCurrentField().removeUnit(zpm);
                    zpm.setCurrentField(null);
                    jaffa.addZPMSimply(zpm);
                }
            }
        }

        if (!boxesToCheck.isEmpty()) {
            for (Box box : boxesToCheck) {
                if (oneill != null && box.getCurrentField() != null && box.getCurrentField().getPosition().x == oneill.getCurrentField().getPosition().x && box.getCurrentField().getPosition().y == oneill.getCurrentField().getPosition().y) { //ha egy mezőn állnak
                    if (!oneill.grabBox(box)) {
                        box.getCurrentField().removeUnit(box);
                        box.setCurrentField(null);
                        box.kill();
                    } else {
                        box.getCurrentField().removeUnit(box);
                        box.setCurrentField(null);
                    }
                } else if (jaffa != null && box.getCurrentField() != null && box.getCurrentField().getPosition().x == jaffa.getCurrentField().getPosition().x && box.getCurrentField().getPosition().y == jaffa.getCurrentField().getPosition().y) {
                    if (!jaffa.grabBox(box)) {
                        box.getCurrentField().removeUnit(box);
                        box.setCurrentField(null);
                        box.kill();
                    } else {
                        box.getCurrentField().removeUnit(box);
                        box.setCurrentField(null);
                    }
                }
            }
        }

        for (Field field : fields) {
            for (Unit unit : units) {
                if (unit.getCurrentField() != null && unit.getCurrentField().equals(field) && !field.getUnits().contains(unit))
                    field.addUnit(unit);
            }
        }


        //checkMap();
    }
    
    public Direction directionByChar(char c) throws Exception{
		switch(c){
		case 'N': return Direction.NORTH;
		case 'E': return Direction.EAST;
		case 'W': return Direction.WEST;
		case 'S': return Direction.SOUTH;
		default:
			throw new Exception("Hiba: �rv�nytelen a megadott ir�ny");
		}
    }
    
    public Color colorByString(String rColor) throws Exception{
		Color color = null;
		if(rColor.equals("blue")){
			color = Color.BLUE;
		}else
		if(rColor.equals("yellow")){
			color = Color.YELLOW;
		}else
		if(rColor.equals("red")){
			color = Color.RED;
		}else
		if(rColor.equals("green")){
			color = Color.GREEN;
		}else
			throw new Exception("Hiba: �rv�nytelen a megadott sz�n");
		return color;
    }
    
    public ActionType actionTypeByString(String type) throws Exception{
		ActionType action = ActionType.NONE;
		if(type.equals("MOVE")){
			action = ActionType.MOVE;
		}else
		if(type.equals("TURN")){
			action = ActionType.TURN;
		}else
		if(type.equals("SHOOT")){
			action = ActionType.SHOOT;
		}else
		if(type.equals("GRAB")){
			action = ActionType.GRAB;
		}else
		if(type.equals("DROP")){
			action = ActionType.DROP;
		}else
		if(type.equals("NONE")){
			action = ActionType.NONE;
		}else
			throw new Exception("Hiba: �rv�nytelen a megadott akci�");
		return action;
    }
    
    
    //helper function
    public void checkMap(){
    	
    	//list fields and neighbours
    	for (Field field : fields) {
    		field.getPosition();
    		String north = (field.getNeighbourInDirection(Direction.NORTH) == null) ? "null" : field.getNeighbourInDirection(Direction.NORTH).toString();
    		String west = (field.getNeighbourInDirection(Direction.WEST) == null) ? "null" : field.getNeighbourInDirection(Direction.WEST).toString();
    		String east = (field.getNeighbourInDirection(Direction.EAST) == null) ? "null" : field.getNeighbourInDirection(Direction.EAST).toString();
    		String south = (field.getNeighbourInDirection(Direction.SOUTH) == null) ? "null" : field.getNeighbourInDirection(Direction.SOUTH).toString();
			System.out.println( field.toString() + " szomsz�dok: "
					+ " \n�szak: " +north
					+ " \nNyugat: " + west
					+ " \nKelet: " + east
					+ " \nD�l: " + south + "\n");
			System.out.println("Rajta lev� egys�g: ");
			field.showUnits();
			System.out.println("\n\n");
		}
    }
    
    public Portal getPortal(){
    	return portal;
    }
    
    public void logOff(){
    	log = false;
    }
    
    public void logOn(){
    	log = true;
    }
    
    public Field getEmptyRoad(){
    	List<Field> emptyRoads = new ArrayList<Field>();
    	
    	for(Field road : roads){
    		if(road.containedUnits.isEmpty()){
    			emptyRoads.add(road);
    		}
    	}
    	
    	Random rand = new Random();
    	int n = rand.nextInt(emptyRoads.size());
    	return emptyRoads.get(n);
    }
    
    public void createZPM(){
    	Field empty = getEmptyRoad();
    	ZPM drop = new ZPM(empty);
    	empty.addUnit(drop);
    	units.add(drop);
    	zpms.add(drop);
        try {
            game.registerDrawableUnit(new DrawableZPM(drop));
        } catch (IOException e){
            //TODO hibakezelés
            e.printStackTrace();
        }

    }

	public void createReplicator(){
		Field empty = getEmptyRoad();
		Replicator rep = new Replicator(game, Direction.EAST, empty);
		empty.addUnit(rep);
		units.add(rep);
		game.setReplicator(rep);
		try {
			game.registerDrawableUnit(new DrawableReplicator(rep));
		} catch (IOException e){
			//TODO hibakezelés
			e.printStackTrace();
		}

	}

    // új lövedék létrehozázásához szebb, ha az új lövedék létrehozásához használt sémát használjuk
    public void createBullet(Bullet bullet){
        units.add(bullet);
        try {
            game.registerDrawableUnit(new DrawableBullet(bullet));
        } catch (IOException e){
            //TODO hibakezelés
            e.printStackTrace();
        }
    }
    
    //given field's must be initialized
    public void replaceField(Field field){
    	Road road = new Road();
    	Field oldField = fields.get(fields.indexOf(field));
    	road.setPosition(oldField.getPosition());

        Field neighbourTemp = field.getNeighbourInDirection(Direction.NORTH);
    	road.addNeighbour(Direction.NORTH, neighbourTemp);
        if(neighbourTemp != null) neighbourTemp.addNeighbour(Direction.SOUTH, road);    //az északi szomszédunknak mi a déli szomszédai vagyunk

        neighbourTemp = field.getNeighbourInDirection(Direction.WEST);
    	road.addNeighbour(Direction.WEST, neighbourTemp);
        if(neighbourTemp != null) neighbourTemp.addNeighbour(Direction.EAST, road);

        neighbourTemp = field.getNeighbourInDirection(Direction.EAST);
    	road.addNeighbour(Direction.EAST, neighbourTemp);
        if(neighbourTemp != null) neighbourTemp.addNeighbour(Direction.WEST, road);

        neighbourTemp = field.getNeighbourInDirection(Direction.SOUTH);
    	road.addNeighbour(Direction.SOUTH, neighbourTemp);
        if(neighbourTemp != null) neighbourTemp.addNeighbour(Direction.NORTH, road);

    	fields.set(fields.indexOf(field), road);

        try { //kitöröljük a field-hez tartozó Drawable-t és beregisztráljuk az új út Drawable-jét
            game.deleteDrawableField(field);
            game.registerDrawableField(new DrawableRoad(road));
        } catch (IOException e){
            e.printStackTrace();
        }

    	boolean isRoad = false;
    	for(Field r : roads){
    		if(r.getPosition().equals(field.getPosition()))
    			isRoad = true;
    	}
    	if(!isRoad)
    		roads.add(road);
    	else
    		roads.set(roads.indexOf(field), road);
    }
    
    //helper query, check boxes
    public List<Unit> listBoxes(){
    	List<Unit> boxes = new ArrayList<Unit>();
    	for(Unit box : units){
    		if(box instanceof Box){
    			boxes.add(box);
    		}
    	}
    	return boxes;
    }
    
    //helper query, check zpm's
    public List<ZPM> listZPM(){
    	return zpms;
    }
    
    public int getOriginalZPMNumber(){    // visszaadja a játék kezdetén lévő zpm-ek darabszámát
    	return allZPM;
    }
    
    public int getAllZPM(){   //visszaadja az összes pályán lévő zpm darabszámát
    	int count = 0;
    	for(ZPM zpm : zpms){
    		if(zpm.getCurrentField() != null)
    			count++;
    	}
    	return count;
    }
    
    public Field getField(Point position){
    	for(Field field : fields){
    		if(field.getPosition().equals(position))
    			return field;
    	}
    	return null;
    }
    
    List<String> getLog(){
    	return lastLog;
    }
    
    public void update(){
    	List<String> before = new ArrayList<String>();
    	List<String> after = new ArrayList<String>();
    	lastLog = new ArrayList<String>();
    	
    	if(log){
	        for(Field field : fields){
	        	before.add(field.toString());
	        	for(Unit fieldUnit : field.getUnits()){
	        		before.add("\t" + fieldUnit.toString());
	        	}
	        }
    	}
    	
        for(int i = 0; i < units.size(); i++){
        	units.get(i).action();
        }
        
    	if(log){
	        for(Field field : fields){
	        	after.add(field.toString());
	        	for(Unit fieldUnit : field.getUnits()){
	        		after.add("\t" + fieldUnit.toString());
	        	}
	        }
	        for(String beforeStr : before){
	        	if(!after.contains(beforeStr)){
	        		lastLog.add(beforeStr);
	        	}
	        }
	        lastLog.add("\n");
	        for(String afterStr : after){
	        	if(!before.contains(afterStr)){
	        		lastLog.add(afterStr);
	        	}
	        }
    	}
    	
    	collectUnits();
    }

    public void collectUnits(){
    	Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
        	Unit nextElement = iterator.next();
			if(nextElement.isDead()){
                if(nextElement.getCurrentField() != null){
				    nextElement.getCurrentField().removeUnit(nextElement);
                }
				iterator.remove();
			}
		}
    	
        for(ZPM zpm : zpms){
        	if(zpm.isDead())
        		zpms.remove(zpm);
        }
    }

    public void killUnit(Unit unit){
    		unit.kill();
    }
    
    public void killUnit(Field field){
    	for(Unit fieldUnit : field.getUnits()){
    		fieldUnit.kill();
    	}
    }
    
    public void setUnitPos(Unit unit, Field field){
    	unit.getCurrentField().removeUnit(unit);
    	unit.setCurrentField(field);
    	field.addUnit(unit);
    }
    
    public void setUnitPos(Field fieldFrom, Field fieldTo){
    	Set<Unit> fromUnits = fieldFrom.getUnits();
    	for(Unit unit : fromUnits){
    		fieldFrom.removeUnit(unit);
    		fieldTo.addUnit(unit);
    		unit.setCurrentField(fieldTo);
    	}
    }
    
    public void addUnit(Unit unit)
    {
    	units.add(unit); 
    }
    
    public void addUnit(ZPM unit)
    {
    	units.add(unit); 
    	zpms.add(unit); 
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Unit> getUnits() {
        return units;
    }
}