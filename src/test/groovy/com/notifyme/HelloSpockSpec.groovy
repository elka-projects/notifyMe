import spock.lang.Specification

class HelloSpockSpec extends spock.lang.Specification {
    def "length of Spock name"() {
        expect:
        name.size() == length

        where:
        name    | length
        "Spock" | 5
        "Kirk"  | 5
    }

    def "collaborators must be invoked in order"()
    {
        def col1 = Mock(Collaborator)
        def col2 = Mock(Collaborator)

        when:
        col1.collaborate()
        col2.collaborate()

        then:
        1 * col1.collaborate()

        then:
        1 * col2.collaborate()
    }

    def "maximum of two numbers"()
    {
        expect:
        Math.max(a,b) == c

        where:
        a << [3,5,9]
        b << [7,4,9]
        c << [7,5,9]
    }

    def "minimum of #a and #b is #c"()
    {
        expect:
        Math.min(a,b) == c

        where:

        a | b ||c
        3 | 7 ||3
        5 | 4 ||4
        9 | 9 ||9
    }
}

interface Collaborator
{
    def collaborate()
}