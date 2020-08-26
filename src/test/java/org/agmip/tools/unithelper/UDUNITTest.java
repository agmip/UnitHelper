package org.agmip.tools.unithelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import org.junit.Test;
import ucar.units.ConversionException;
import ucar.units.Converter;
import ucar.units.NameException;
import ucar.units.NoSuchUnitException;
import ucar.units.Prefix;
import ucar.units.PrefixDB;
import ucar.units.PrefixDBException;
import ucar.units.PrefixDBManager;
import ucar.units.PrefixName;
import ucar.units.PrefixSymbol;
import ucar.units.ScaledUnit;
import ucar.units.SpecificationException;
import ucar.units.Unit;
import ucar.units.UnitClassException;
import ucar.units.UnitDB;
import ucar.units.UnitDBAccessException;
import ucar.units.UnitDBException;
import ucar.units.UnitDBManager;
import ucar.units.UnitExistsException;
import ucar.units.UnitFormat;
import ucar.units.UnitFormatManager;
import ucar.units.UnitName;
import ucar.units.UnitParseException;
import ucar.units.UnitSystem;
import ucar.units.UnitSystemException;
import ucar.units.UnitSystemManager;

/**
 *
 * @author mike
 */
public class UDUNITTest {
    
    private static UnitFormat parser = UnitFormatManager.instance();

//    @Test
    public static void test() throws InterruptedException, UnitDBException, ConversionException, UnitParseException, SpecificationException, NoSuchUnitException, PrefixDBException, UnitSystemException, UnitClassException, UnitExistsException, UnitDBAccessException, NameException {
//        UnitDAO.testRun();
        UnitSystem system = UnitSystemManager.instance();
        Iterator itBase = system.getBaseUnitDB().getIterator();
        while(itBase.hasNext()) {
            Unit item = (Unit) itBase.next();
            System.out.println(item.getSymbol() + "\t" + item.getDerivedUnit().getQuantityDimension() + "\t" + item.getUnitName() + "\t" + item.getCanonicalString() + "\t" + item.getName());
        }
        
        System.out.println("**********************************************");

        UnitDB db = UnitDBManager.instance();
        Iterator it = db.getIterator();
        db.addUnit(new ScaledUnit(0.1, parser.parse("kg"), UnitName.newUnitName("100g")));
//        while(it.hasNext()) {
//            Unit item = (Unit) it.next();
//            System.out.println(item.getSymbol() + "\t" + item.getDerivedUnit().getQuantityDimension() + "\t" + item.getUnitName() + "\t" + item.getCanonicalString() + "\t" + item.getName());
//        }
        listUnit("t");
        System.out.println("**********************************************");
        
//        System.out.println("degC:");
//        printInfo(parser.parse("degC"));
//        System.out.println("degF:");
//        printInfo(parser.parse("degF"));
//        System.out.println("K:");
//        printInfo(parser.parse("K"));
//        System.out.println("test");
//        printInfo(parser.parse("sidereal day"));
//        printInfo(parser.parse("sidereal_day"));
        printInfo(parser.parse("kg/kg"));
        printInfo(parser.parse("g/kg"));
        printInfo(parser.parse("deg"));
        System.out.println("**********************************************");
        
        Unit unitFrom = parser.parse("cm");
        Unit unitTo = parser.parse("m");
        Unit unit = new ScaledUnit(7, unitFrom);
        Converter conv = unitFrom.getConverterTo(unitTo);
        System.out.println(conv.convert(1));
        
        testBD("6400", "6401", "64.01", "1.333", "0.01", "64.0001", "0.9999");
        System.out.println(new BigDecimal("1").divide(new BigDecimal(3), 2, RoundingMode.HALF_UP).doubleValue());
        
        System.out.println(UnitConverter.listUnitJsonStr("M"));
        System.out.println("**********************************************");
        
        PrefixDB prefixDB = PrefixDBManager.instance();
        Iterator itpre = prefixDB.iterator();
        while (itpre.hasNext()) {
            PrefixName name = (PrefixName) itpre.next();
            Prefix prefix = prefixDB.getPrefixByName(name.toString());
            System.out.print(prefix.toString() + "\t");
            System.out.print(prefix.getID() + "\t");
            System.out.print(prefix.getValue() + "\t");
            System.out.println(prefixDB.getPrefixByValue(prefix.getValue()).toString());
        }
        
        String prefixDBDump = prefixDB.toString();
        System.out.println(prefixDBDump);
        int start = prefixDBDump.indexOf("symbolSet=[");
        String symbolDump = prefixDBDump.substring(start + 11).replaceAll("\\].*", "");
        System.out.println(symbolDump);
        String[] symbols = symbolDump.split(",\\s*");
        for (String symbol : symbols) {
            PrefixSymbol prefix = (PrefixSymbol) prefixDB.getPrefixBySymbol(symbol);
            System.out.print(prefix.toString() + "\t");
            System.out.print(prefix.getID() + "\t");
            System.out.print(prefix.getValue() + "\t");
            System.out.println(prefixDB.getPrefixByValue(prefix.getValue()).toString());
        }
        
    }
    
    private static void printInfo(Unit unit) throws UnitClassException {
        System.out.println("Name: " + unit.getName());
        System.out.println("Symbol: " + unit.getSymbol());
        System.out.println("Plural: " + unit.getPlural());
        System.out.println("UnitName: " + unit.getUnitName());
        System.out.println("CanonicalString: " + unit.getCanonicalString());
        System.out.println("toString: " + unit.toString());
        System.out.println("Dimension: " + unit.getDerivedUnit().getDimension());
        System.out.println("QuantityDimension: " + unit.getDerivedUnit().getQuantityDimension());
        System.out.println("Dimensionless: " + unit.isDimensionless());
        System.out.println("format: " + parser.format(unit));
    }
    
    private static void testBD(String... vals) {
        System.out.println("\tprec\tscale\tdouble\tprec+1\tx100\t/100");
        for (String val : vals) {
            BigDecimal test = new BigDecimal(val);
            System.out.print(val + "\t");
            System.out.print(test.precision() + "\t");
            System.out.print(test.scale() + "\t");
            System.out.print(test.doubleValue() + "\t");
            System.out.print(test.setScale(test.precision()+1).doubleValue() + "\t");
            System.out.print(test.multiply(new BigDecimal(100)).setScale(test.precision()+1).doubleValue() + "\t");
            System.out.print(test.divide(new BigDecimal(100), RoundingMode.HALF_UP).setScale(test.precision()+1).doubleValue() + "\t");
            System.out.println();
        }
//        System.out.println("precision: " + test.precision());
//        System.out.println("scale: " + test.scale());
//        System.out.println("double: " + test.doubleValue());
//        System.out.println("double+1: " + test.setScale(test.precision()+1).doubleValue());
    }
    
    private static void listUnit(String type) throws UnitDBException {
        UnitDB db = UnitDBManager.instance();
        Iterator it = db.getIterator();
        if (it.hasNext()) {
            System.out.println("Symbol\tDimen\tUName\tCanStr\tName");
        }
        while(it.hasNext()) {
            Unit item = (Unit) it.next();
            if (item.getDerivedUnit().getQuantityDimension().toString().equals(type)) {
                System.out.println(item.getSymbol() + "\t" + item.getDerivedUnit().getQuantityDimension() + "\t" + item.getUnitName() + "\t" + item.getCanonicalString() + "\t" + item.getName());
            }
        }
    }
}
