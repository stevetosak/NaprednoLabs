package Labs2.Zadaca1;

class DateHandler implements Comparable<DateHandler> {
    private long date;

    DateHandler(){}
    DateHandler(String date){
        StringBuilder format = new StringBuilder(date);
        format.deleteCharAt(4);
        format.deleteCharAt(6);
        this.date = Long.parseLong(format.toString());
    }


    @Override
    public int compareTo(DateHandler o) {
        return Long.compare(this.date,o.date);
    }
}
